/*
 * Port của avatar/service/NoService.java
 * @author kitakeyos - Hoàng Hữu Dũng
 */
import { MapService } from '../play/MapService.js';

export class NoService extends MapService {

  static getInstance() {
    return instance;
  }

  constructor(cl) {
    super(cl);
  }

  sendMessage(ms) {
  }

  chat(user, text) {
  }

  move(us) {
  }
}

const instance = new NoService(null);

export const noService = instance;
export default NoService;
