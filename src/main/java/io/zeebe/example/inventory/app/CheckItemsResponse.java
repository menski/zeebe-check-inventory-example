package io.zeebe.example.inventory.app;

import java.util.List;

public class CheckItemsResponse {

  private String checkId;
  private List<CheckItemResult> items;

  public String getCheckId() {
    return checkId;
  }

  public CheckItemsResponse setCheckId(String checkId) {
    this.checkId = checkId;
    return this;
  }

  public List<CheckItemResult> getItems() {
    return items;
  }

  public CheckItemsResponse setItems(List<CheckItemResult> items) {
    this.items = items;
    return this;
  }

  @Override
  public String toString() {
    return "CheckItemsResponse{" +
      "checkId=" + checkId +
      ", items=" + items +
      '}';
  }
}
