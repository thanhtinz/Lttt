/** Port của avatar/network/Message.java */
import { DataOutputStream, DataInputStream } from './JavaIO.js';

export class Message {
  /**
   * @param {number} command
   * @param {Buffer=} data nếu có -> message để ĐỌC; không có -> message để GHI
   */
  constructor(command, data = null) {
    this.command = ((Number(command) | 0) << 24) >> 24; // ép về byte có dấu
    if (data != null) {
      this.is = new DataInputStream(data);
      this.os = null;
    } else {
      this.os = new DataOutputStream();
      this.is = null;
    }
  }

  getCommand() {
    return this.command;
  }

  setCommand(cmd) {
    this.command = ((Number(cmd) | 0) << 24) >> 24;
  }

  getData() {
    return this.os ? this.os.toBuffer() : Buffer.alloc(0);
  }

  /** @returns {DataInputStream} */
  reader() {
    return this.is;
  }

  /** @returns {DataOutputStream} */
  writer() {
    return this.os;
  }

  cleanup() {
    // Không cần giải phóng thủ công như Java, giữ API cho tương thích
  }
}

export default Message;
