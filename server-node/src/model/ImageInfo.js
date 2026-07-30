/** Port của avatar/model/ImageInfo.java (@Builder @AllArgsConstructor @Getter). */
export class ImageInfo {
  constructor(id = 0, bigImageID = 0, x = 0, y = 0, w = 0, h = 0) {
    this.id = id | 0;
    this.bigImageID = bigImageID | 0;
    this.x = x | 0;
    this.y = y | 0;
    this.w = w | 0;
    this.h = h | 0;
  }

  getId() { return this.id; }
  getBigImageID() { return this.bigImageID; }
  getX() { return this.x; }
  getY() { return this.y; }
  getW() { return this.w; }
  getH() { return this.h; }

  static builder() {
    const f = {};
    const b = {
      build: () => new ImageInfo(f.id, f.bigImageID, f.x, f.y, f.w, f.h),
    };
    for (const k of ['id', 'bigImageID', 'x', 'y', 'w', 'h']) {
      b[k] = (v) => { f[k] = v; return b; };
    }
    return b;
  }
}

export default ImageInfo;
