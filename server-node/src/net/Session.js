/**
 * Port phần MẠNG của avatar/network/Session.java.
 *
 * Giữ nguyên hành vi:
 *  - handshakeMessage(): gửi khoá XOR (lệnh -27) rồi bật `connected`.
 *  - sendMessage(): xếp hàng rồi ghi ra socket (Java dùng Sender thread; Node
 *    dùng luồng ghi bất đồng bộ của socket nên không cần thread riêng).
 *  - Đọc: gom byte -> Message -> chuyển cho messageHandler.onMessage().
 *
 * Phần XỬ LÝ LỆNH (getHandler, các service...) nằm ở handler/, không ở đây.
 */
import { Message } from './Message.js';
import { SessionCodec, FrameParser } from './SessionCodec.js';

export class Session {
  /**
   * @param {import('net').Socket} socket
   * @param {number} id
   */
  constructor(socket, id) {
    this.sc = socket;
    this.id = id;
    this.ip = (socket.remoteAddress || '').replace(/^::ffff:/, '');
    this.user = null;
    this.login = false;
    this.connected = false;
    this.platform = null;
    this.versionARM = null;
    this.resourceType = 0;

    this.codec = new SessionCodec();
    this.parser = new FrameParser(this.codec);
    /** @type {{onMessage:(m:Message)=>any, onDisconnected?:()=>any}|null} */
    this.messageHandler = null;

    socket.setNoDelay(true);
    socket.on('data', (chunk) => this._onData(chunk));
    socket.on('error', () => this.close());
    socket.on('close', () => this._onClose());
  }

  isConnected() {
    return this.connected;
  }

  setHandler(h) {
    this.messageHandler = h;
  }

  /** Gửi 1 Message tới client. */
  sendMessage(msg) {
    if (this.sc.destroyed) return;
    try {
      this.sc.write(this.codec.encode(msg));
      msg.cleanup();
    } catch { /* socket đã đóng */ }
  }

  /** Gửi khoá XOR cho client rồi bật chế độ mã hoá. */
  handshakeMessage() {
    const buf = this.codec.buildHandshake();
    this.sc.write(buf);
    this.codec.connected = true;
    this.connected = true;
  }

  isResourceHD() {
    return this.resourceType === 1;
  }

  _onData(chunk) {
    this.parser.push(chunk);
    let m;
    try {
      while ((m = this.parser.shift()) !== null) {
        if (this.messageHandler) {
          Promise.resolve(this.messageHandler.onMessage(m)).catch((e) =>
            console.error('[session] lỗi xử lý lệnh', m.getCommand(), e)
          );
        }
      }
    } catch (e) {
      console.error('[session] lỗi khung tin:', e.message);
      this.close();
    }
  }

  _onClose() {
    if (this._closed) return;
    this._closed = true;
    this.connected = false;
    if (this.messageHandler?.onDisconnected) {
      try { this.messageHandler.onDisconnected(); } catch { /* ignore */ }
    }
  }

  close() {
    this.connected = false;
    if (!this.sc.destroyed) this.sc.destroy();
  }

  toString() {
    return this.user ? String(this.user) : `Client ${this.id}`;
  }
}

export default Session;
