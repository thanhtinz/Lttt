/**
 * Port của avatar/db/DbManager.java — pool kết nối MySQL.
 * Đọc cấu hình từ database.properties (giữ đúng định dạng của server Java).
 */
import fs from 'fs';
import path from 'path';
import mysql from 'mysql2/promise';

/** Đọc file .properties kiểu Java thành object. */
export function loadProperties(file) {
  const out = {};
  if (!fs.existsSync(file)) return out;
  for (const raw of fs.readFileSync(file, 'utf8').split(/\r?\n/)) {
    const line = raw.trim();
    if (!line || line.startsWith('#') || line.startsWith('!')) continue;
    const i = line.indexOf('=');
    if (i < 0) continue;
    out[line.slice(0, i).trim()] = line.slice(i + 1).trim();
  }
  return out;
}

class DbManager {
  constructor() {
    this.pool = null;
    this.props = {};
  }

  loadConfig(baseDir = process.cwd()) {
    const p = loadProperties(path.join(baseDir, 'database.properties'));
    this.props = {
      host: p.host || '127.0.0.1',
      port: Number(p.port || 3306),
      dbname: p.dbname || 'avatar_2x',
      // Java dùng cả "password" và "passeword" (lỗi chính tả trong bản gốc)
      username: p.username || 'root',
      password: p.password ?? p.passeword ?? '',
      maxConnections: Number(p.max_connection || 100),
      minConnections: Number(p.min_connection || 10),
    };
    return this.props;
  }

  async start(baseDir = process.cwd()) {
    if (this.pool) {
      console.warn('DB Connection Pool has already been created.');
      return this.pool;
    }
    const c = this.props.host ? this.props : this.loadConfig(baseDir);
    this.pool = mysql.createPool({
      host: c.host,
      port: c.port,
      user: c.username,
      password: c.password,
      database: c.dbname,
      waitForConnections: true,
      connectionLimit: c.maxConnections,
      charset: 'utf8mb4',
      dateStrings: true,
    });
    await this.pool.query('SELECT 1');
    console.log(`[db] MySQL ${c.dbname}@${c.host}:${c.port} OK`);
    return this.pool;
  }

  /** Tương đương QueryRunner.query — trả về mảng dòng. */
  async query(sql, params = []) {
    const [rows] = await this.pool.query(sql, params);
    return rows;
  }

  /** Lấy 1 dòng hoặc null. */
  async queryOne(sql, params = []) {
    const rows = await this.query(sql, params);
    return rows[0] || null;
  }

  /** Tương đương executeUpdate — trả về số dòng ảnh hưởng. */
  async executeUpdate(sql, params = []) {
    const [res] = await this.pool.query(sql, params);
    return res.affectedRows ?? 0;
  }

  /** INSERT trả về insertId. */
  async insert(sql, params = []) {
    const [res] = await this.pool.query(sql, params);
    return res.insertId;
  }

  async close() {
    if (this.pool) {
      await this.pool.end();
      this.pool = null;
    }
  }
}

export const dbManager = new DbManager();
export default dbManager;
