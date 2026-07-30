/**
 * Kiểm tra tương thích nhị phân với server Java.
 *
 * Các chuỗi hex "golden" dưới đây được sinh bằng CHÍNH java.io.DataOutputStream
 * và logic Session.doSendMessage của bản Java (khoá XOR cố định để tái lập được).
 * Nếu test này đỏ nghĩa là client cũ sẽ không hiểu gói tin nữa.
 */
import test from 'node:test';
import assert from 'node:assert/strict';
import { DataOutputStream, DataInputStream } from '../src/net/JavaIO.js';
import { Message } from '../src/net/Message.js';
import { SessionCodec, FrameParser } from '../src/net/SessionCodec.js';

test('DataOutputStream khớp byte với java.io.DataOutputStream', () => {
  const d = new DataOutputStream();
  d.writeByte(-5);
  d.writeByte(200);
  d.writeShort(-1234);
  d.writeShort(60000);
  d.writeInt(-99999);
  d.writeLong(1735689600000n);
  d.writeBoolean(true);
  d.writeFloat(1.5);
  d.writeDouble(-2.25);
  d.writeUTF('Xin chao Avatar');
  d.writeUTF('Tiếng Việt có dấu');
  d.writeUTF('');
  d.writeUTF('x' + String.fromCharCode(0) + 'y'); // NUL -> 2 byte (modified UTF-8)
  d.writeUTF('⚔');

  const golden =
    'fbc8fb2eea60fffe7961000001941f297c00013fc00000c00200000000000000' +
    '0f58696e206368616f2041766174617200185469e1babf6e67205669e1bb8774' +
    '2063c3b32064e1baa5750000000478c080790003e29a94';
  assert.equal(d.toBuffer().toString('hex'), golden);
});

test('readUTF đọc lại đúng chuỗi có dấu', () => {
  const d = new DataOutputStream();
  d.writeUTF('Tiếng Việt có dấu');
  d.writeUTF('⚔ ok');
  const r = new DataInputStream(d.toBuffer());
  assert.equal(r.readUTF(), 'Tiếng Việt có dấu');
  assert.equal(r.readUTF(), '⚔ ok');
});

test('đóng khung tin khớp Session.java (handshake + XOR + lệnh 90)', () => {
  const key = Buffer.from('1700000000000_kitakeyos', 'latin1');
  const c = new SessionCodec(key);

  const parts = [c.buildHandshake()];
  c.connected = true;

  const m1 = new Message(-1);
  m1.writer().writeUTF('xin chào');
  m1.writer().writeInt(12345);
  m1.writer().writeByte(-3);
  parts.push(c.encode(m1));

  const m2 = new Message(75);
  m2.writer().writeShort(777);
  m2.writer().writeUTF('admin');
  parts.push(c.encode(m2));

  const m3 = new Message(90); // lệnh 90: size = writeInt, data KHÔNG xor
  m3.writer().write(Buffer.from([1, 2, 3, 4, 5]));
  parts.push(c.encode(m3));

  const golden =
    'e5001817310607000000000000000000006f34021d150a0e1c161cce37203039' +
    '48595e105358f390306b694458962e79667038373551545d595e6a0000000501' +
    '02030405';
  assert.equal(Buffer.concat(parts).toString('hex'), golden);
});

test('FrameParser giải mã lại đúng message đã đóng khung', () => {
  const srv = new SessionCodec();
  const cli = new SessionCodec(srv.key);

  const p = new FrameParser(cli);
  p.push(srv.buildHandshake());
  srv.connected = true;

  const hs = p.shift();
  assert.equal(hs.getCommand(), -27);
  assert.equal(hs.reader().readUnsignedByte(), srv.key.length);
  cli.connected = true;

  const m = new Message(42);
  m.writer().writeUTF('nội dung');
  m.writer().writeInt(-7);
  p.push(srv.encode(m));

  const got = p.shift();
  assert.equal(got.getCommand(), 42);
  assert.equal(got.reader().readUTF(), 'nội dung');
  assert.equal(got.reader().readInt(), -7);
});

test('FrameParser xử lý được gói tin bị chia nhỏ (TCP fragmentation)', () => {
  const srv = new SessionCodec();
  const cli = new SessionCodec(srv.key);
  const p = new FrameParser(cli);
  p.push(srv.buildHandshake());
  p.shift();
  srv.connected = true;
  cli.connected = true;

  const m = new Message(9);
  m.writer().writeUTF('chia nhỏ từng byte');
  const buf = srv.encode(m);

  // nạp từng byte một: chỉ khi đủ khung mới trả về message
  for (let i = 0; i < buf.length - 1; i++) {
    p.push(buf.subarray(i, i + 1));
    assert.equal(p.shift(), null, 'chưa đủ byte thì phải trả null');
  }
  p.push(buf.subarray(buf.length - 1));
  const got = p.shift();
  assert.equal(got.getCommand(), 9);
  assert.equal(got.reader().readUTF(), 'chia nhỏ từng byte');
});
