/** Port của avatar/lib/KeyValue.java — tương đương Map.Entry<K,V>. */
export class KeyValue {
  constructor(key, value) {
    this.key = key;
    this.value = value;
  }

  getKey() {
    return this.key;
  }

  getValue() {
    return this.value;
  }

  setKey(key) {
    return (this.key = key);
  }

  setValue(value) {
    return (this.value = value);
  }
}

export default KeyValue;
