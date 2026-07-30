/* Client game top-down 2D — Phaser 3 + Socket.IO.
 * Nhân vật cozy dạng nhiều lớp: base (da) + clothes + hair + eyes.
 * Sheet walk: grid 32x32, 8 frame/hướng, 4 hàng = 4 hướng.
 */
const TILE = 16;
const CHAR = 32;
const SPEED = 110;

// Hàng trong sheet ứng với hướng
const DIR_ROW = { down: 0, up: 1, left: 2, right: 3 };

// Frame nền lấy từ tileset ground.png (54 cột)
const GROUND_COLS = 54;
const T_GRASS = 55;
const T_GRASS2 = 69;
const T_DIRT = 82;
const T_WATER = 96;

const CATALOG = {
  base: ['char1', 'char2', 'char3', 'char4', 'char5', 'char6', 'char7', 'char8'],
  clothes: ['basic', 'dress', 'overalls', 'pants', 'sailor', 'skirt', 'sporty', 'suit', 'witch'],
  clothesColors: 10,
  hair: ['bob', 'braids', 'buzzcut', 'curly', 'emo', 'ponytail', 'spacebuns', 'wavy'],
  hairColors: 14,
  eyesColors: 14,
};

/* ---------- RNG có seed (để mọi client sinh cùng bản đồ) ---------- */
function mulberry32(seed) {
  let a = seed >>> 0;
  return function () {
    a = (a + 0x6d2b79f5) >>> 0;
    let t = Math.imul(a ^ (a >>> 15), 1 | a);
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

/* ---------- Sprite nhân vật nhiều lớp ---------- */
class Character {
  /** @param {Phaser.Scene} scene */
  constructor(scene, state) {
    this.scene = scene;
    this.state = state;
    this.frameIdx = 0;
    this.animTime = 0;

    this.container = scene.add.container(state.x, state.y);
    this.container.setDepth(state.y);

    this.shadow = scene.add.image(0, 13, 'shadow').setScale(1.3, 0.9).setAlpha(0.35);

    const a = state.appearance;
    this.layers = [];
    // thứ tự vẽ: da -> quần áo -> mắt -> tóc
    this.baseSpr = scene.add.sprite(0, 0, `base_${CATALOG.base[a.base]}`, 0);
    this.clothesSpr = scene.add.sprite(0, 0, `clothes_${CATALOG.clothes[a.clothes]}`, 0);
    this.eyesSpr = scene.add.sprite(0, 0, 'eyes', 0);
    this.hairSpr = scene.add.sprite(0, 0, `hair_${CATALOG.hair[a.hair]}`, 0);
    this.layers = [this.baseSpr, this.clothesSpr, this.eyesSpr, this.hairSpr];

    this.label = scene.add
      .text(0, -22, state.name, { fontFamily: 'monospace', fontSize: '9px', color: '#ffffff' })
      .setOrigin(0.5)
      .setStroke('#000000', 3);

    this.bubble = scene.add
      .text(0, -34, '', {
        fontFamily: 'monospace', fontSize: '9px', color: '#1a1f2b',
        backgroundColor: '#ffffffdd', padding: { x: 3, y: 2 }, wordWrap: { width: 110 },
      })
      .setOrigin(0.5, 1)
      .setVisible(false);

    this.container.add([this.shadow, ...this.layers, this.label, this.bubble]);
    this.applyFrame();
  }

  /** Số cột của 1 sheet (để tính frame theo biến thể màu). */
  sheetCols(key) {
    const tex = this.scene.textures.get(key);
    return Math.floor(tex.getSourceImage().width / CHAR);
  }

  applyFrame() {
    const a = this.state.appearance;
    const row = DIR_ROW[this.state.dir] ?? 0;
    const f = this.frameIdx;

    const setFrame = (spr, variant) => {
      const cols = this.sheetCols(spr.texture.key);
      spr.setFrame(row * cols + variant * 8 + f);
    };
    setFrame(this.baseSpr, 0); // sheet base chỉ có 1 biến thể
    setFrame(this.clothesSpr, a.clothesColor);
    setFrame(this.eyesSpr, a.eyesColor);
    setFrame(this.hairSpr, a.hairColor);
  }

  update(dt) {
    if (this.state.moving) {
      this.animTime += dt;
      if (this.animTime >= 100) {
        this.animTime = 0;
        this.frameIdx = (this.frameIdx + 1) % 8;
        this.applyFrame();
      }
    } else if (this.frameIdx !== 0) {
      this.frameIdx = 0;
      this.animTime = 0;
      this.applyFrame();
    }
    this.container.setDepth(this.container.y);
  }

  setPos(x, y) {
    this.container.setPosition(x, y);
  }

  setDir(dir) {
    if (this.state.dir !== dir) {
      this.state.dir = dir;
      this.applyFrame();
    }
  }

  say(text) {
    this.bubble.setText(text).setVisible(true);
    clearTimeout(this._bt);
    this._bt = setTimeout(() => this.bubble.setVisible(false), 4000);
  }

  destroy() {
    clearTimeout(this._bt);
    this.container.destroy();
  }
}

/* ---------- Scene chính ---------- */
class World extends Phaser.Scene {
  constructor() {
    super('World');
    this.others = new Map();
  }

  preload() {
    this.load.image('shadow', 'assets/char/shadow.png');
    this.load.spritesheet('ground', 'assets/world/ground.png', { frameWidth: TILE, frameHeight: TILE });
    this.load.image('nature', 'assets/world/nature.png');

    const cfg = { frameWidth: CHAR, frameHeight: CHAR };
    for (const b of CATALOG.base) this.load.spritesheet(`base_${b}`, `assets/char/base/${b}_walk.png`, cfg);
    for (const c of CATALOG.clothes) this.load.spritesheet(`clothes_${c}`, `assets/char/clothes/${c}_walk.png`, cfg);
    for (const h of CATALOG.hair) this.load.spritesheet(`hair_${h}`, `assets/char/hair/${h}_walk.png`, cfg);
    this.load.spritesheet('eyes', 'assets/char/eyes/eyes_walk.png', cfg);
  }

  create() {
    this.socket = window.__socket;
    const boot = window.__boot; // {you, world, seed, players}
    this.world = boot.world;

    this.buildMap(boot.seed);

    // nhân vật của mình
    this.me = new Character(this, { ...boot.you });
    this.pos = { x: boot.you.x, y: boot.you.y };

    // người chơi khác đã có sẵn
    for (const [id, st] of Object.entries(boot.players)) {
      if (id !== boot.you.id) this.others.set(id, new Character(this, st));
    }

    this.cameras.main.setBounds(0, 0, this.world.width, this.world.height);
    this.cameras.main.startFollow(this.me.container, true, 0.12, 0.12);
    this.cameras.main.setZoom(2.4);
    this.cameras.main.roundPixels = true;

    this.keys = this.input.keyboard.addKeys('W,A,S,D,UP,DOWN,LEFT,RIGHT');
    this.lastSent = 0;

    this.bindSocket();
  }

  /** Sinh bản đồ nền (cỏ + hồ nước + đường đất + cây) theo seed. */
  buildMap(seed) {
    const rnd = mulberry32(seed);
    const cols = Math.floor(this.world.width / TILE);
    const rows = Math.floor(this.world.height / TILE);

    const data = [];
    for (let r = 0; r < rows; r++) {
      const line = [];
      for (let c = 0; c < cols; c++) line.push(rnd() < 0.12 ? T_GRASS2 : T_GRASS);
      data.push(line);
    }

    // hồ nước
    const lx = Math.floor(cols * 0.68), ly = Math.floor(rows * 0.28);
    for (let r = ly; r < ly + 12; r++) {
      for (let c = lx; c < lx + 16; c++) {
        const dx = (c - (lx + 8)) / 8, dy = (r - (ly + 6)) / 6;
        if (dx * dx + dy * dy < 1 && data[r]) data[r][c] = T_WATER;
      }
    }
    // đường đất chữ thập
    const midR = Math.floor(rows / 2), midC = Math.floor(cols / 2);
    for (let c = 0; c < cols; c++) for (let r = midR - 1; r <= midR + 1; r++) if (data[r][c] !== T_WATER) data[r][c] = T_DIRT;
    for (let r = 0; r < rows; r++) for (let c = midC - 1; c <= midC + 1; c++) if (data[r][c] !== T_WATER) data[r][c] = T_DIRT;

    const map = this.make.tilemap({ data, tileWidth: TILE, tileHeight: TILE });
    const ts = map.addTilesetImage('ground');
    map.createLayer(0, ts, 0, 0).setDepth(-1000);

    this.waterTiles = data;
    this.cols = cols;
    this.rows = rows;
  }

  bindSocket() {
    const s = this.socket;
    s.on('player_join', (st) => {
      if (!this.others.has(st.id) && st.id !== this.me.state.id) this.others.set(st.id, new Character(this, st));
    });
    s.on('player_move', (m) => {
      const o = this.others.get(m.id);
      if (!o) return;
      o.state.moving = m.moving;
      o.setDir(m.dir);
      o.target = { x: m.x, y: m.y };
    });
    s.on('player_leave', (id) => {
      const o = this.others.get(id);
      if (o) { o.destroy(); this.others.delete(id); }
    });
    s.on('chat', ({ id, name, text }) => {
      const who = id === this.me.state.id ? this.me : this.others.get(id);
      if (who) who.say(text);
      const log = document.getElementById('chatlog');
      const d = document.createElement('div');
      d.textContent = `${name}: ${text}`;
      log.appendChild(d);
      log.scrollTop = log.scrollHeight;
    });
  }

  /** Ô có đi được không (chặn nước). */
  walkable(x, y) {
    const c = Math.floor(x / TILE), r = Math.floor(y / TILE);
    if (c < 0 || r < 0 || c >= this.cols || r >= this.rows) return false;
    return this.waterTiles[r][c] !== T_WATER;
  }

  update(_time, delta) {
    const k = this.keys;
    let vx = 0, vy = 0;
    if (k.A.isDown || k.LEFT.isDown) vx = -1;
    else if (k.D.isDown || k.RIGHT.isDown) vx = 1;
    if (k.W.isDown || k.UP.isDown) vy = -1;
    else if (k.S.isDown || k.DOWN.isDown) vy = 1;

    const moving = vx !== 0 || vy !== 0;
    if (moving) {
      const len = Math.hypot(vx, vy) || 1;
      const step = (SPEED * delta) / 1000;
      const nx = this.pos.x + (vx / len) * step;
      const ny = this.pos.y + (vy / len) * step;
      // thử trục X rồi trục Y để trượt dọc tường
      if (this.walkable(nx, this.pos.y + 8)) this.pos.x = nx;
      if (this.walkable(this.pos.x, ny + 8)) this.pos.y = ny;

      // hướng: ưu tiên trục có chuyển động lớn hơn
      const dir = Math.abs(vx) > Math.abs(vy) ? (vx < 0 ? 'left' : 'right') : (vy < 0 ? 'up' : 'down');
      this.me.setDir(dir);
    }
    this.me.state.moving = moving;
    this.me.setPos(this.pos.x, this.pos.y);
    this.me.update(delta);

    // nội suy người chơi khác
    for (const o of this.others.values()) {
      if (o.target) {
        const c = o.container;
        c.x += (o.target.x - c.x) * 0.2;
        c.y += (o.target.y - c.y) * 0.2;
      }
      o.update(delta);
    }

    // gửi vị trí ~15/s
    this.lastSent += delta;
    if (this.lastSent > 66) {
      this.lastSent = 0;
      this.socket.emit('move', {
        x: this.pos.x, y: this.pos.y, dir: this.me.state.dir, moving,
      });
    }
  }
}

/* ---------- Khởi động: đăng nhập rồi vào game ---------- */
const socket = io();
window.__socket = socket;

const $ = (id) => document.getElementById(id);
const ri = (n) => Math.floor(Math.random() * n);

function pickAppearance() {
  return {
    base: +$('sel-base').value,
    clothes: +$('sel-clothes').value,
    clothesColor: +$('sel-cc').value,
    hair: +$('sel-hair').value,
    hairColor: +$('sel-hc').value,
    eyesColor: +$('sel-ec').value,
  };
}

function fillSelects() {
  const opt = (sel, n, labels) => {
    sel.innerHTML = '';
    for (let i = 0; i < n; i++) {
      const o = document.createElement('option');
      o.value = i;
      o.textContent = labels ? labels[i] : `#${i + 1}`;
      sel.appendChild(o);
    }
    sel.value = ri(n);
  };
  opt($('sel-base'), CATALOG.base.length);
  opt($('sel-clothes'), CATALOG.clothes.length, CATALOG.clothes);
  opt($('sel-cc'), CATALOG.clothesColors);
  opt($('sel-hair'), CATALOG.hair.length, CATALOG.hair);
  opt($('sel-hc'), CATALOG.hairColors);
  opt($('sel-ec'), CATALOG.eyesColors);
}
fillSelects();

let started = false;
function startGame(boot) {
  if (started) return;
  started = true;
  window.__boot = boot;
  $('login').style.display = 'none';
  $('hud').style.display = 'block';
  new Phaser.Game({
    type: Phaser.AUTO,
    parent: 'game',
    pixelArt: true,
    backgroundColor: '#5b7f4e',
    scale: { mode: Phaser.Scale.RESIZE, width: '100%', height: '100%' },
    scene: [World],
  });
}

socket.on('welcome', startGame);
socket.on('login_error', (m) => { $('err').textContent = m; });

$('btn-login').onclick = () =>
  socket.emit('login', { username: $('u').value, password: $('p').value, appearance: pickAppearance() });
$('btn-guest').onclick = () =>
  socket.emit('login', { guest: true, username: $('u').value, appearance: pickAppearance() });

$('chatform').onsubmit = (e) => {
  e.preventDefault();
  const v = $('chatinput').value.trim();
  if (v) socket.emit('chat', v);
  $('chatinput').value = '';
  $('chatinput').blur();
};
