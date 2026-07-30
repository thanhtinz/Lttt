/**
 * Port của avatar/server/Avatar.java — các helper file tĩnh.
 * Phần main()/start() đã nằm ở src/index.js.
 */
import fs from 'fs';

export class Avatar {
  /**
   * Đọc file thành mảng byte (Java: getFile).
   * @param {string} url đường dẫn file
   * @returns {Buffer|null} null nếu lỗi — giống bản Java trả null
   */
  static getFile(url) {
    try {
      return fs.readFileSync(url);
    } catch (e) {
      return null;
    }
  }

  /** Kích thước file, 0 nếu lỗi (Java in stack trace rồi trả 0). */
  static getFileSize(url) {
    try {
      return fs.statSync(url).size;
    } catch (e) {
      console.error(`getFileSize: ${url}`, e.message);
      return 0;
    }
  }

  /** Ghi đè file bằng mảng byte. */
  static saveFile(url, ab) {
    try {
      fs.writeFileSync(url, Buffer.isBuffer(ab) ? ab : Buffer.from(ab));
    } catch (e) {
      console.error(`saveFile: ${url}`, e.message);
    }
  }
}

export const getFile = Avatar.getFile;
export const getFileSize = Avatar.getFileSize;
export const saveFile = Avatar.saveFile;
export default Avatar;
