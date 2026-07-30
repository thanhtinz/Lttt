/** Port của avatar/play/Zone.java */
import { Npc } from '../model/Npc.js';
import { MapService } from './MapService.js';

export class Zone {

  constructor(map, id) {
    this.map = map;
    this.id = id;
    this.players = [];
    this.service = new MapService(null);
    this.service.setZone(this);
  }

  find(id) {
    for (const us of this.players) {
      if (us.getId() === id) {
        return us;
      }
    }
    return null;
  }

  add(us) {
    this.players.push(us);
  }

  remove(us) {
    const idx = this.players.indexOf(us);
    if (idx >= 0) {
      this.players.splice(idx, 1);
    }
  }

  enter(us, x, y) {
    try {
      const zone = us.getZone();
      if (zone != null) {
        zone.leave(us);
      }
      us.setZone(this);
      us.setX(x);
      us.setY(y);
      if (!(us instanceof Npc)) {
        this.getService().addPlayer(us);
        us.getService().weather(2);
        us.getAvatarService().enter(this);
      }
      this.add(us);
    } catch (ex) {
      console.error('Zone.enter()', ex);
    }
  }

  leave(user) {
    this.remove(user);
    this.getService().leavePark(user.getId());
    user.setZone(null);
  }

  update() {

  }

  // ==== getter/setter (lombok @Getter @Setter) ====
  getMap() { return this.map; }
  setMap(v) { this.map = v; }

  getId() { return this.id; }
  setId(v) { this.id = v | 0; }

  getPlayers() { return this.players; }
  setPlayers(v) { this.players = v; }

  getService() { return this.service; }
  setService(v) { this.service = v; }
}

export default Zone;
