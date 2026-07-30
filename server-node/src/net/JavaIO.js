/**
 * Tương thích nhị phân với java.io.DataOutputStream / DataInputStream.
 * Bắt buộc phải khớp từng byte với server Java để client cũ vẫn chạy được.
 *
 * Lưu ý quan trọng: writeUTF/readUTF dùng "modified UTF-8" của Java
 * (tiền tố độ dài 2 byte unsigned, ký tự NUL mã hoá 2 byte, cặp surrogate
 * mã hoá thành hai chuỗi 3 byte — tức CESU-8).
 */

export class DataOutputStream {
  constructor(initial = 256) {
    this.buf = Buffer.alloc(initial);
    this.len = 0;
  }

  _ensure(n) {
    if (this.len + n <= this.buf.length) return;
    let cap = this.buf.length * 2;
    while (cap < this.len + n) cap *= 2;
    const nb = Buffer.alloc(cap);
    this.buf.copy(nb, 0, 0, this.len);
    this.buf = nb;
  }

  writeByte(v) {
    this._ensure(1);
    this.buf.writeInt8(((Number(v) | 0) << 24) >> 24, this.len);
    this.len += 1;
    return this;
  }

  writeBoolean(v) {
    return this.writeByte(v ? 1 : 0);
  }

  writeShort(v) {
    this._ensure(2);
    this.buf.writeInt16BE((((Number(v) | 0) << 16) >> 16), this.len);
    this.len += 2;
    return this;
  }

  writeChar(v) {
    this._ensure(2);
    this.buf.writeUInt16BE(Number(v) & 0xffff, this.len);
    this.len += 2;
    return this;
  }

  writeInt(v) {
    this._ensure(4);
    this.buf.writeInt32BE(Number(v) | 0, this.len);
    this.len += 4;
    return this;
  }

  writeLong(v) {
    this._ensure(8);
    this.buf.writeBigInt64BE(BigInt(v), this.len);
    this.len += 8;
    return this;
  }

  writeFloat(v) {
    this._ensure(4);
    this.buf.writeFloatBE(Number(v), this.len);
    this.len += 4;
    return this;
  }

  writeDouble(v) {
    this._ensure(8);
    this.buf.writeDoubleBE(Number(v), this.len);
    this.len += 8;
    return this;
  }

  /** Ghi mảng byte thô (tương đương DataOutputStream.write(byte[])). */
  write(bytes) {
    const b = Buffer.isBuffer(bytes) ? bytes : Buffer.from(bytes);
    this._ensure(b.length);
    b.copy(this.buf, this.len);
    this.len += b.length;
    return this;
  }

  /** Java modified UTF-8, tiền tố 2 byte độ dài. */
  writeUTF(str) {
    const s = String(str ?? '');
    // tính độ dài byte trước
    let n = 0;
    for (let i = 0; i < s.length; i++) {
      const c = s.charCodeAt(i);
      if (c >= 0x0001 && c <= 0x007f) n += 1;
      else if (c === 0 || c <= 0x07ff) n += 2;
      else n += 3;
    }
    if (n > 0xffff) throw new Error('writeUTF: chuỗi quá dài (' + n + ' byte)');
    this._ensure(2 + n);
    this.buf.writeUInt16BE(n, this.len);
    this.len += 2;
    for (let i = 0; i < s.length; i++) {
      const c = s.charCodeAt(i);
      if (c >= 0x0001 && c <= 0x007f) {
        this.buf[this.len++] = c;
      } else if (c === 0 || c <= 0x07ff) {
        this.buf[this.len++] = 0xc0 | ((c >> 6) & 0x1f);
        this.buf[this.len++] = 0x80 | (c & 0x3f);
      } else {
        this.buf[this.len++] = 0xe0 | ((c >> 12) & 0x0f);
        this.buf[this.len++] = 0x80 | ((c >> 6) & 0x3f);
        this.buf[this.len++] = 0x80 | (c & 0x3f);
      }
    }
    return this;
  }

  flush() { /* no-op: ghi vào buffer trong bộ nhớ */ }

  toBuffer() {
    return Buffer.from(this.buf.subarray(0, this.len));
  }

  size() {
    return this.len;
  }
}

export class DataInputStream {
  constructor(buffer) {
    this.buf = Buffer.isBuffer(buffer) ? buffer : Buffer.from(buffer || []);
    this.pos = 0;
  }

  available() {
    return Math.max(0, this.buf.length - this.pos);
  }

  _need(n) {
    if (this.pos + n > this.buf.length) {
      const e = new Error('EOF');
      e.eof = true;
      throw e;
    }
  }

  readByte() {
    this._need(1);
    return this.buf.readInt8(this.pos++);
  }

  readUnsignedByte() {
    this._need(1);
    return this.buf.readUInt8(this.pos++);
  }

  readBoolean() {
    return this.readByte() !== 0;
  }

  readShort() {
    this._need(2);
    const v = this.buf.readInt16BE(this.pos);
    this.pos += 2;
    return v;
  }

  readUnsignedShort() {
    this._need(2);
    const v = this.buf.readUInt16BE(this.pos);
    this.pos += 2;
    return v;
  }

  readChar() {
    return this.readUnsignedShort();
  }

  readInt() {
    this._need(4);
    const v = this.buf.readInt32BE(this.pos);
    this.pos += 4;
    return v;
  }

  /** Trả về Number (đủ cho mốc thời gian ms); dùng readLongBig nếu cần chính xác 64-bit. */
  readLong() {
    return Number(this.readLongBig());
  }

  readLongBig() {
    this._need(8);
    const v = this.buf.readBigInt64BE(this.pos);
    this.pos += 8;
    return v;
  }

  readFloat() {
    this._need(4);
    const v = this.buf.readFloatBE(this.pos);
    this.pos += 4;
    return v;
  }

  readDouble() {
    this._need(8);
    const v = this.buf.readDoubleBE(this.pos);
    this.pos += 8;
    return v;
  }

  /** Đọc n byte thô. */
  readBytes(n) {
    this._need(n);
    const out = Buffer.from(this.buf.subarray(this.pos, this.pos + n));
    this.pos += n;
    return out;
  }

  /** Java modified UTF-8. */
  readUTF() {
    const n = this.readUnsignedShort();
    this._need(n);
    const end = this.pos + n;
    let out = '';
    while (this.pos < end) {
      const a = this.buf[this.pos];
      if (a >> 7 === 0) {
        this.pos += 1;
        out += String.fromCharCode(a);
      } else if (a >> 5 === 0b110) {
        const b = this.buf[this.pos + 1];
        this.pos += 2;
        out += String.fromCharCode(((a & 0x1f) << 6) | (b & 0x3f));
      } else {
        const b = this.buf[this.pos + 1];
        const c = this.buf[this.pos + 2];
        this.pos += 3;
        out += String.fromCharCode(((a & 0x0f) << 12) | ((b & 0x3f) << 6) | (c & 0x3f));
      }
    }
    return out;
  }
}
