package io.zeebe.example.inventory.app;

import java.util.List;

public class CheckItemsRequest {

  private List<String> items;

  public List<String> getItems() {
    return items;
  }

  public CheckItemsRequest setItems(List<String> items) {
    this.items = items;
    return this;
  }

  @Override
  public String toString() {
    return "CheckItemsMessage{" + "items=" + items + '}';
  }
}
