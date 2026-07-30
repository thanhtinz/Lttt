/**
 * Kiểm tra việc THAY ASSET chạy thật qua protocol:
 * đăng nhập rồi gửi REQUEST_TILE_MAP, so byte ảnh server trả về với file cozy trên đĩa.
 * Nếu khớp ⇒ mọi client (Java jar, APK, Unity) sẽ nhận đúng art mới.
 *
 * Chạy: node test/e2e-tilemap.mjs [user] [pass] [idTile]
 */
import net from 'net';
import fs from 'fs';
import { Message } from '../src/net/Message.js';
import { SessionCodec, FrameParser } from '../src/net/SessionCodec.js';
import { Cmd } from '../src/constants/Cmd.js';

const [user = 'admin', pass = 'admin123', idTile = '1'] = process.argv.slice(2);
const expected = fs.readFileSync(new URL('../res/hd/tilemap/' + idTile + '.png', import.meta.url));

const codec = new SessionCodec(Buffer.alloc(1));
const parser = new FrameParser(codec);
let handshakeDone = false;
let asked = false;
let result = null;

const sock = net.createConnection({ host: '127.0.0.1', port: 19128 });
const send = (m) => sock.write(codec.encode(m));

sock.on('data', (chunk) => {
  parser.push(chunk);
  let m;
  while ((m = parser.shift()) !== null) {
    const cmd = m.getCommand();

    if (!handshakeDone && cmd === -27) {
      const r = m.reader();
      const len = r.readUnsignedByte();
      const key = Buffer.alloc(len);
      key[0] = r.readUnsignedByte();
      for (let i = 1; i < len; i++) key[i] = (r.readUnsignedByte() ^ key[i - 1]) & 0xff;
      codec.key = key;
      codec.reset();
      codec.connected = true;
      handshakeDone = true;

      const info = new Message(Cmd.SET_PROVIDER);
      const w = info.writer();
      w.writeByte(0); w.writeInt(2000000); w.writeUTF('Nokia240x320');
      w.writeInt(0); w.writeInt(240); w.writeInt(320); w.writeBoolean(false);
      w.writeByte(1); // resourceType = 1 -> dùng res/hd
      send(info);

      const login = new Message(Cmd.LOGIN);
      login.writer().writeUTF(user);
      login.writer().writeUTF(pass);
      login.writer().writeUTF('2.5.8');
      send(login);
      continue;
    }

    if (cmd === Cmd.LOGIN_SUCESS && !asked) {
      asked = true;
      console.log('→ đăng nhập OK, xin tileset id=' + idTile);
      const req = new Message(Cmd.REQUEST_TILE_MAP);
      req.writer().writeByte(Number(idTile));
      send(req);
      continue;
    }

    if (cmd === Cmd.REQUEST_TILE_MAP) {
      const r = m.reader();
      const id = r.readByte();
      const png = r.readBytes(r.available());
      result = { id, png };
      console.log(`← nhận tileset id=${id}, ${png.length} byte`);
      const same = Buffer.compare(png, expected) === 0;
      console.log(`   PNG magic: ${png.subarray(1, 4).toString()} | khớp file cozy trên đĩa: ${same ? 'CÓ' : 'KHÔNG'}`);
      console.log(same
        ? '✅ THAY ASSET HOẠT ĐỘNG: server gửi đúng tileset cozy qua protocol gốc'
        : '❌ byte không khớp');
      sock.end();
      process.exit(same ? 0 : 1);
    }
  }
});

sock.on('error', (e) => { console.error('LỖI socket:', e.message); process.exit(1); });
setTimeout(() => {
  console.error('❌ hết thời gian chờ — chưa nhận được tileset');
  process.exit(1);
}, 8000);
