// Cấu hình server game Node.js
export const config = {
  port: Number(process.env.PORT || 3000),
  world: { width: 1600, height: 1600 }, // kích thước bản đồ (px)
  // MySQL: tái dùng DB avatar_2x nếu có. Không có DB -> chế độ khách (guest).
  db: {
    enabled: process.env.DB_ENABLED !== '0',
    host: process.env.DB_HOST || '127.0.0.1',
    port: Number(process.env.DB_PORT || 3306),
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASS || '',
    database: process.env.DB_NAME || 'avatar_2x',
  },
};
