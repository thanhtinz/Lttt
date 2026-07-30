/**
 * Điểm khởi động — port của avatar/server/Avatar.java
 * Chạy: npm start   (đọc config.properties + database.properties cùng thư mục)
 */
import serverManager from './server/ServerManager.js';

const BANNER = `     _                      _                      ____
    / \\    __   __   __ _  | |_    __ _   _ __    / ___|    ___   _ __  __   __   ___   _ __
   / _ \\   \\ \\ / /  / _\` | | __|  / _\` | | '__|   \\___ \\   / _ \\ | '__| \\ \\ / /  / _ \\ | '__|
  / ___ \\   \\ V /  | (_| | | |_  | (_| | | |       ___) | |  __/ | |     \\ V /  |  __/ | |
 /_/   \\_\\   \\_/    \\__,_|  \\__|  \\__,_| |_|      |____/   \\___| |_|      \\_/    \\___| |_|   (Node.js)`;

async function main() {
  console.log(BANNER);

  for (const sig of ['SIGINT', 'SIGTERM']) {
    process.on(sig, () => {
      console.log('Shutdown Server!');
      serverManager.stop();
      process.exit(0);
    });
  }

  await serverManager.init();

  // Gắn handler cho mỗi kết nối: bắt tay rồi chuyển tiếp cho MessageHandler
  const { MessageHandler } = await import('./message/MessageHandler.js');
  serverManager.listen((session) => {
    session.setHandler(new MessageHandler(session));
    session.handshakeMessage();
  });
}

main().catch((e) => {
  console.error('Lỗi khởi động:', e);
  process.exit(1);
});
