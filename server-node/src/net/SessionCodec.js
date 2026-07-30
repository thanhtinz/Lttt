/**
 * Mã hoá/giải mã khung tin theo đúng avatar/network/Session.java:
 *
 *  - Khoá XOR: bytes của `${Date.now()}_kitakeyos` (Java: System.currentTimeMillis() + "_kitakeyos"),
 *    tạo 1 lần cho cả tiến trình (Java dùng static final).
 *  - Handshake (lệnh -27): gửi độ dài khoá + key[0] + (key[i] ^ key[i-1]); sau đó bật `connected`.
 *  - Khi CHƯA connected: cmd thô, size = writeShort, data thô.
 *  - Khi ĐÃ connected: cmd và 2 byte size đều qua writeKey; toàn bộ data qua writeKey.
 *  - Ngoại lệ: lệnh 90 dùng writeInt cho size và KHÔNG XOR phần data.
 *
 * Lưu ý: con trỏ khoá curR/curW là byte có dấu trong Java; ở đây mô phỏng
 * đúng hành vi tăng rồi lấy dư theo độ dài khoá.
 */
import { Message } from './Message.js';
import { DataOutputStream } from './JavaIO.js';

/** Khoá XOR dùng chung toàn tiến trình (giống static final trong Java). */
export const SESSION_KEY = Buffer.from(`${Date.now()}_kitakeyos`, 'latin1');

export class SessionCodec {
  constructor(key = SESSION_KEY) {
    this.key = key;
    this.curR = 0;
    this.curW = 0;
    this.connected = false;
  }

  readKey(b) {
    const i = (this.key[this.curR] & 0xff) ^ (b & 0xff);
    this.curR = (this.curR + 1) % this.key.length;
    return i & 0xff;
  }

  writeKey(b) {
    const i = (this.key[this.curW] & 0xff) ^ (b & 0xff);
    this.curW = (this.curW + 1) % this.key.length;
    return i & 0xff;
  }

  reset() {
    this.curR = 0;
    this.curW = 0;
  }

  /** Đóng khung 1 Message thành Buffer để ghi ra socket. */
  encode(msg) {
    const data = msg.getData();
    const out = [];
    const cmd = msg.getCommand() & 0xff;

    out.push(this.connected ? this.writeKey(cmd) : cmd);

    if (msg.getCommand() === 90) {
      const head = Buffer.alloc(4);
      head.writeInt32BE(data.length, 0);
      return Buffer.concat([Buffer.from(out), head, data]);
    }

    const size = data.length;
    if (this.connected) {
      out.push(this.writeKey((size >> 8) & 0xff));
      out.push(this.writeKey(size & 0xff));
      const enc = Buffer.alloc(size);
      for (let i = 0; i < size; i++) enc[i] = this.writeKey(data[i]);
      return Buffer.concat([Buffer.from(out), enc]);
    }

    const head = Buffer.alloc(2);
    head.writeUInt16BE(size, 0);
    return Buffer.concat([Buffer.from(out), head, data]);
  }

  /** Buffer handshake (lệnh -27) — gửi khoá cho client. */
  buildHandshake() {
    const ms = new Message(-27);
    const ds = ms.writer();
    ds.writeByte(this.key.length);
    ds.writeByte(this.key[0]);
    for (let i = 1; i < this.key.length; i++) {
      ds.writeByte(this.key[i] ^ this.key[i - 1]);
    }
    return this.encode(ms); // encode trước khi bật connected (giống Java)
  }
}

/**
 * Bộ gom byte từ socket -> Message, đúng logic MessageCollector của Java.
 * Dùng cho luồng TCP: nạp dữ liệu bằng push(), lấy ra bằng vòng lặp shift().
 */
export class FrameParser {
  constructor(codec) {
    this.codec = codec;
    this.buf = Buffer.alloc(0);
  }

  push(chunk) {
    this.buf = this.buf.length ? Buffer.concat([this.buf, chunk]) : chunk;
  }

  /** Lấy 1 Message nếu đã đủ byte, ngược lại trả null. */
  shift() {
    const connected = this.codec.connected;
    const need = 3; // 1 byte cmd + 2 byte size
    if (this.buf.length < need) return null;

    // Thử đọc header mà KHÔNG tiêu thụ con trỏ khoá cho tới khi chắc đủ data.
    const savedR = this.codec.curR;
    let cmd = this.buf[0];
    if (connected) cmd = this.codec.readKey(cmd);

    let size;
    if (connected) {
      size = ((this.codec.readKey(this.buf[1]) & 0xff) << 8) | (this.codec.readKey(this.buf[2]) & 0xff);
    } else {
      size = this.buf.readUInt16BE(1);
    }

    if (this.buf.length < 3 + size) {
      this.codec.curR = savedR; // chưa đủ -> hoàn tác con trỏ khoá
      return null;
    }

    let data = Buffer.from(this.buf.subarray(3, 3 + size));
    if (connected) {
      for (let i = 0; i < data.length; i++) data[i] = this.codec.readKey(data[i]);
    }
    this.buf = Buffer.from(this.buf.subarray(3 + size));

    return new Message(((cmd | 0) << 24) >> 24, data);
  }
}

export default SessionCodec;
