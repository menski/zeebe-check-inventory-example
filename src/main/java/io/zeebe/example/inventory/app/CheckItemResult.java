package io.zeebe.example.inventory.app;

public class CheckItemResult {

  private String item;
  private boolean available;

  public String getItem() {
    return item;
  }

  public CheckItemResult setItem(String item) {
    this.item = item;
    return this;
  }

  public boolean isAvailable() {
    return available;
  }

  public CheckItemResult setAvailable(boolean available) {
    this.available = available;
    return this;
  }

  @Override
  public String toString() {
    return "CheckItemResult{" + "item='" + item + '\'' + ", available=" + available + '}';
  }
}
