/** Port của avatar/Farm/PhanBon.java. */
export class PhanBon {
  constructor(id = 0, soluong = 0) {
    this.id = id | 0;       // ID của hạt giống
    this.soluong = soluong | 0; // Số lượng của hạt giống
  }

  getId() { return this.id; }
  setId(id) { this.id = id | 0; }

  getSoluong() { return this.soluong; }
  setSoluong(soluong) { this.soluong = soluong | 0; }

  // Phương thức in thông tin về hạt giống
  printInfo() {
    console.log('ID Hạt giống: ' + this.id + ', Số lượng: ' + this.soluong);
  }
}

export default PhanBon;
