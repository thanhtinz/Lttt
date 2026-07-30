/**
 * Port của avatar/Farm/Animal.java.
 * NOTE: JS không cho vừa field `isAlive` vừa method `isAlive()` ⇒ các cờ boolean
 * lưu ở `_isAlive`/`_isReadyForBreeding`/`_isHarvestable`, truy cập qua method
 * đúng tên Java (isAlive(), setAlive(), ...).
 */
export class Animal {
  constructor(id = 0, health = 0, level = 0, resourceCount = 0, nextProductionTime = 0,
              isAlive = false, isReadyForBreeding = false, isHarvestable = false) {
    this.id = id | 0;                        // id vat nuoi
    this.health = health | 0;                // Sức khỏe của động vật
    this.level = level | 0;                  // Cấp độ của động vật
    this.resourceCount = resourceCount | 0;  // Số tài nguyên (sản phẩm) mà động vật tạo ra
    this.nextProductionTime = nextProductionTime | 0; // Thời gian tạo sản phẩm tiếp theo
    this._isAlive = !!isAlive;               // Trạng thái động vật có còn sống không
    this._isReadyForBreeding = !!isReadyForBreeding; // Sẵn sàng sinh sản không
    this._isHarvestable = !!isHarvestable;   // Có thể thu hoạch sản phẩm không
  }

  // Getters và Setters
  getId() { return this.id; }
  setId(Id) { this.id = Id | 0; }

  getHealth() { return this.health; }
  setHealth(health) { this.health = health | 0; }

  getLevel() { return this.level; }
  setLevel(level) { this.level = level | 0; }

  getResourceCount() { return this.resourceCount; }
  setResourceCount(resourceCount) { this.resourceCount = resourceCount | 0; }

  getNextProductionTime() { return this.nextProductionTime; }
  setNextProductionTime(nextProductionTime) { this.nextProductionTime = nextProductionTime | 0; }

  isAlive() { return this._isAlive; }
  setAlive(alive) { this._isAlive = !!alive; }

  isReadyForBreeding() { return this._isReadyForBreeding; }
  setReadyForBreeding(readyForBreeding) { this._isReadyForBreeding = !!readyForBreeding; }

  isHarvestable() { return this._isHarvestable; }
  setHarvestable(harvestable) { this._isHarvestable = !!harvestable; }

  getType() {
    // Giả sử ID động vật phụ thuộc vào cấp độ
    return ((50 + (this.level % 7)) << 24) >> 24; // (byte)
  }

  toString() {
    return 'Animal{'
      + 'health=' + this.health
      + ', level=' + this.level
      + ', resourceCount=' + this.resourceCount
      + ', nextProductionTime=' + this.nextProductionTime
      + ', isAlive=' + this._isAlive
      + ', isReadyForBreeding=' + this._isReadyForBreeding
      + ', isHarvestable=' + this._isHarvestable
      + '}';
  }
}

export default Animal;
