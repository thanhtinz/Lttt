// Server game top-down 2D (Node.js): Express phục vụ client + Socket.IO realtime.
import express from 'express';
import { createServer } from 'http';
import { Server } from 'socket.io';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';
import crypto from 'crypto';
import { config } from './config.js';
import { initDb, dbReady, authUser, loadPlayer, savePosition } from './db.js';
import { CATALOG, randomAppearance, sanitizeAppearance } from './appearance.js';

const __dirname = dirname(fileURLToPath(import.meta.url));
const app = express();
const http = createServer(app);
const io = new Server(http, { cors: { origin: '*' } });

app.use(express.static(join(__dirname, '..', 'public')));
app.get('/health', (_req, res) => res.json({ ok: true, db: dbReady(), players: players.size }));
app.get('/catalog', (_req, res) => res.json(CATALOG));

const md5 = (s) => crypto.createHash('md5').update(String(s)).digest('hex');

/** Trạng thái người chơi trong bộ nhớ: socket.id -> state */
const players = new Map();

/** Seed cố định để mọi client sinh cùng một bản đồ. */
const WORLD_SEED = Number(process.env.WORLD_SEED || 20260730);

const snapshot = () => Object.fromEntries(players);

io.on('connection', (socket) => {
  socket.on('login', async (data = {}) => {
    let name = String(data.username || '').slice(0, 16).trim();
    let userId = null;
    let x = config.world.width / 2;
    let y = config.world.height / 2;

    // Ngoại hình: client chọn, server kẹp lại; không có thì random
    const appearance = data.appearance ? sanitizeAppearance(data.appearance) : randomAppearance();

    if (!data.guest && dbReady() && name) {
      const u = await authUser(name, md5(data.password || ''));
      if (!u) {
        socket.emit('login_error', 'Sai tài khoản hoặc mật khẩu.');
        return;
      }
      userId = u.id;
      name = u.username;
      const p = await loadPlayer(u.id);
      if (p) {
        if (p.pos_x != null) x = Number(p.pos_x);
        if (p.pos_y != null) y = Number(p.pos_y);
      }
    } else if (!name) {
      name = 'Khách' + Math.floor(Math.random() * 1000);
    }

    const state = { id: socket.id, userId, name, appearance, x, y, dir: 'down', moving: false };
    players.set(socket.id, state);

    socket.emit('welcome', {
      you: state,
      world: config.world,
      seed: WORLD_SEED,
      players: snapshot(),
    });
    socket.broadcast.emit('player_join', state);
    console.log(`[join] ${name} (${socket.id}) — tổng ${players.size}`);
  });

  socket.on('move', (m = {}) => {
    const p = players.get(socket.id);
    if (!p) return;
    p.x = Math.max(0, Math.min(config.world.width, Number(m.x) || 0));
    p.y = Math.max(0, Math.min(config.world.height, Number(m.y) || 0));
    if (['down', 'up', 'left', 'right'].includes(m.dir)) p.dir = m.dir;
    p.moving = !!m.moving;
    socket.broadcast.emit('player_move', { id: socket.id, x: p.x, y: p.y, dir: p.dir, moving: p.moving });
  });

  socket.on('chat', (msg) => {
    const p = players.get(socket.id);
    if (!p) return;
    const text = String(msg || '').slice(0, 120).trim();
    if (text) io.emit('chat', { id: socket.id, name: p.name, text });
  });

  socket.on('disconnect', async () => {
    const p = players.get(socket.id);
    if (!p) return;
    if (p.userId != null) await savePosition(p.userId, Math.round(p.x), Math.round(p.y));
    players.delete(socket.id);
    io.emit('player_leave', socket.id);
    console.log(`[leave] ${p.name} — tổng ${players.size}`);
  });
});

// Lưu vị trí định kỳ cho người chơi đã đăng nhập
setInterval(() => {
  for (const p of players.values()) {
    if (p.userId != null) savePosition(p.userId, Math.round(p.x), Math.round(p.y));
  }
}, 30000);

await initDb();
http.listen(config.port, () => {
  console.log(`🎮 Game server: http://localhost:${config.port}  (DB: ${dbReady() ? 'on' : 'guest'})`);
});
