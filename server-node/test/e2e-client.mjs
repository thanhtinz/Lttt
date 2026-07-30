/**
 * Client TCP giả lập (dùng đúng codec đã verify khớp Java) để test end-to-end:
 * kết nối -> nhận handshake -> gửi SET_PROVIDER + LOGIN -> in các lệnh server trả về.
 *
 * Chạy: node test/e2e-client.mjs [host] [port] [user] [pass]
 */
import net from 'net';
import { Message } from '../src/net/Message.js';
import { SessionCodec, FrameParser } from '../src/net/SessionCodec.js';
import { Cmd } from '../src/constants/Cmd.js';

const [host = '127.0.0.1', port = '19128', user = 'admin', pass = 'admin'] = process.argv.slice(2);

const codec = new SessionCodec(Buffer.alloc(1)); // khoá tạm, sẽ thay bằng khoá server gửi
const parser = new FrameParser(codec);
const received = [];

const sock = net.createConnection({ host, port: Number(port) }, () => {
  console.log(`→ đã kết nối ${host}:${port}`);
});

function send(msg) {
  sock.write(codec.encode(msg));
}

let handshakeDone = false;

sock.on('data', (chunk) => {
  parser.push(chunk);
  let m;
  while ((m = parser.shift()) !== null) {
    const cmd = m.getCommand();
    received.push(cmd);

    if (!handshakeDone && cmd === -27) {
      // Giải mã khoá XOR: byte[0]=len, byte[1]=key[0], byte[i]=key[i]^key[i-1]
      const r = m.reader();
      const len = r.readUnsignedByte();
      const key = Buffer.alloc(len);
      key[0] = r.readUnsignedByte();
      for (let i = 1; i < len; i++) key[i] = (r.readUnsignedByte() ^ key[i - 1]) & 0xff;
      console.log(`← handshake: nhận khoá XOR ${len} byte = "${key.toString('latin1')}"`);

      codec.key = key;
      codec.reset();
      codec.connected = true;
      handshakeDone = true;

      // Gửi thông tin client (SET_PROVIDER) rồi LOGIN — như client thật
      const info = new Message(Cmd.SET_PROVIDER);
      const w = info.writer();
      w.writeByte(0);            // provider
      w.writeInt(2000000);       // memory
      w.writeUTF('Nokia240x320');// platform
      w.writeInt(0);             // rmsSize
      w.writeInt(240);           // width
      w.writeInt(320);           // height
      w.writeBoolean(false);
      w.writeByte(0);            // resourceType (0 = medium)
      send(info);
      console.log(`→ gửi SET_PROVIDER (${Cmd.SET_PROVIDER})`);

      const login = new Message(Cmd.LOGIN);
      login.writer().writeUTF(user);
      login.writer().writeUTF(pass); // server tự md5 (Utils.md5) như bản Java
      login.writer().writeUTF('2.5.8');
      send(login);
      console.log(`→ gửi LOGIN (${Cmd.LOGIN}) user="${user}"`);
      continue;
    }

    // In các lệnh server trả về sau login
    const names = Object.entries(Cmd).filter(([, v]) => v === cmd).map(([k]) => k);
    let extra = '';
    try {
      if (cmd === Cmd.SERVER_MESSAGE || cmd === Cmd.SERVER_DIALOG || cmd === Cmd.SERVER_INFO) {
        extra = ' → "' + m.reader().readUTF() + '"';
      }
    } catch { /* payload khác định dạng */ }
    console.log(`← lệnh ${cmd}${names.length ? ' (' + names.join('/') + ')' : ''}, ${m.reader().available()} byte payload${extra}`);
  }
});

sock.on('error', (e) => {
  console.error('LỖI socket:', e.message);
  process.exit(1);
});

setTimeout(() => {
  console.log('\n=== TỔNG KẾT ===');
  console.log('số gói nhận được:', received.length);
  console.log('các lệnh:', received.join(', '));
  const ok = handshakeDone && received.length > 1;
  console.log(ok ? '✅ Bắt tay OK và server có phản hồi sau LOGIN' : '❌ Không đủ phản hồi');
  sock.end();
  process.exit(ok ? 0 : 1);
}, 4000);
