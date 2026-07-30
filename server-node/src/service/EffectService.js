/** Port của avatar/service/EffectService.java */
import { Cmd } from '../constants/Cmd.js';
import { Message } from '../net/Message.js';

/** Danh sách field của Lombok @Builder — mặc định 0/null như Java. */
const EFFECT_FIELDS = ['session', 'id', 'style', 'loopLimit', 'num', 'timeStop',
  'loop', 'loopType', 'radius', 'positions', 'idPlayer', 'position'];

export class EffectService {

  /**
   * Lombok: @Builder(builderMethodName = "createEffect", buildMethodName = "send")
   * ⇒ EffectService.createEffect().session(s).id(1)... .send()
   */
  static createEffect() {
    const f = {
      session: null, id: 0, style: 0, loopLimit: 0, num: 0, timeStop: 0,
      loop: 0, loopType: 0, radius: 0, positions: null, idPlayer: 0, position: null,
    };
    const b = {
      send: () => EffectService.sendEffect(f.session, f.id, f.style, f.loopLimit, f.num,
        f.timeStop, f.loop, f.loopType, f.radius, f.positions, f.idPlayer, f.position),
    };
    for (const k of EFFECT_FIELDS) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }

  static sendEffect(session, id, style, loopLimit, num, timeStop, loop, loopType, radius,
                    positions, idPlayer, position) {
    try {
      if (session == null) {
        //console.error('Session is null, cannot send effect.');
        return;
      }

      const ms = new Message(Cmd.EFFECT_OBJ);
      const ds = ms.writer();
      ds.writeByte(0);
      ds.writeByte(id);
      ds.writeByte(style);
      ds.writeByte(loopLimit);

      if (style === 4) {
        ds.writeShort(num);
        ds.writeByte(timeStop);
      } else {
        ds.writeShort(loop);
        ds.writeByte(loopType);
        if (loopType === 1) {
          ds.writeShort(radius);
        } else if (loopType === 2) {
          if (positions == null) {
            console.error('Positions array is null, cannot write positions.');
            return;
          }
          ds.writeByte(positions.length);
          for (const p of positions) {
            if (p == null) {
              console.error('Position in positions array is null, cannot write position.');
              continue;
            }
            ds.writeShort(p.getX());
            ds.writeShort(p.getY());
          }
        }
        if (style === 0) {
          ds.writeInt(idPlayer);
        } else {
          ds.writeShort(position.getX());
          ds.writeShort(position.getY());
        }
      }

      ds.flush();
      session.sendMessage(ms);
    } catch (ex) {
      console.error(ex);
    }
  }
}

export default EffectService;
