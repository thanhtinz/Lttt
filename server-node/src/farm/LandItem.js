/**
 * Port của avatar/Farm/LandItem.java.
 * plantedTime: Java LocalDateTime → Date của JS.
 * NOTE: các cờ boolean lưu ở `_isWatered`/`_isFertilized`/`_isHarvestable`
 * vì JS không cho vừa field `isWatered` vừa method `isWatered()`.
 */
export class LandItem {
  constructor(growthTime = 0, type = 0, suckhoe = 0, resourceCount = 0,
              isWatered = false, isFertilized = false, isHarvestable = false, plantedTime = null) {
    this.growthTime = growthTime | 0;       // Thời gian cần để cây trưởng thành
    this.type = type | 0;
    this.sucKhoe = suckhoe | 0;
    this.resourceCount = resourceCount | 0; // Số lượng tài nguyên trong ô đất
    this._isWatered = !!isWatered;          // Trạng thái tưới nước
    this._isFertilized = !!isFertilized;    // Trạng thái đã bón phân
    this._isHarvestable = !!isHarvestable;  // Trạng thái có thể thu hoạch
    this.plantedTime = plantedTime;         // Thời điểm gieo trồng
  }

  getPlantedTime() { return this.plantedTime; }
  setPlantedTime(plantedTime) { this.plantedTime = plantedTime; }

  // Getters và Setters
  getGrowthTime() { return this.growthTime; }
  setGrowthTime(growthTime) { this.growthTime = growthTime | 0; }

  getType() { return this.type; }
  setType(type) { this.type = type | 0; }

  getSucKhoe() { return this.sucKhoe; }
  setSucKhoe(sucKhoe) { this.sucKhoe = sucKhoe | 0; }

  getResourceCount() { return this.resourceCount; }
  setResourceCount(resourceCount) { this.resourceCount = resourceCount | 0; }

  isWatered() { return this._isWatered; }
  setWatered(watered) { this._isWatered = !!watered; }

  isFertilized() { return this._isFertilized; }
  setFertilized(fertilized) { this._isFertilized = !!fertilized; }

  isHarvestable() { return this._isHarvestable; }
  setHarvestable(harvestable) { this._isHarvestable = !!harvestable; }

  toString() {
    // NOTE: giữ nguyên hành vi bản Java (in sucKhoe ở chỗ "type=")
    return 'Land{'
      + 'growthTime=' + this.growthTime
      + ', type=' + this.sucKhoe
      + ', resourceCount=' + this.resourceCount
      + ', isWatered=' + this._isWatered
      + ', isFertilized=' + this._isFertilized
      + ', isHarvestable=' + this._isHarvestable
      + '}';
  }

  getMinutesSincePlanted() {
    if (this.plantedTime == null) {
      throw new Error('plantedTime is not set'); // IllegalStateException
    }

    const now = Date.now();
    const t = this.plantedTime instanceof Date ? this.plantedTime.getTime() : Number(this.plantedTime);
    return Math.trunc((now - t) / 60000); // Duration.toMinutes()
  }
}

export default LandItem;
