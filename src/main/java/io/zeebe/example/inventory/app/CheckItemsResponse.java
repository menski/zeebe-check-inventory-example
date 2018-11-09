package io.zeebe.example.inventory.app;

import java.util.List;

public class CheckItemsResponse {

  private List<CheckItemResult> items;

  public List<CheckItemResult> getItems() {
    return items;
  }

  public CheckItemsResponse setItems(List<CheckItemResult> items) {
    this.items = items;
    return this;
  }

  @Override
  public String toString() {
    return "CheckItemsResponse{" + "items=" + items + '}';
  }
}
