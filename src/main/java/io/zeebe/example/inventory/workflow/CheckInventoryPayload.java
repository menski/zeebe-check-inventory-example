package io.zeebe.example.inventory.workflow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.zeebe.example.inventory.app.CheckItemResult;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class CheckInventoryPayload {
  private String checkId;
  private String sessionId;
  private List<String> items;
  private List<CheckItemResult> results;
  private boolean finished;
  private CheckItemResult message;

  public String getCheckId() {
    return checkId;
  }

  public CheckInventoryPayload setCheckId(String checkId) {
    this.checkId = checkId;
    return this;
  }

  public String getSessionId() {
    return sessionId;
  }

  public CheckInventoryPayload setSessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  public List<String> getItems() {
    return items;
  }

  public CheckInventoryPayload setItems(List<String> items) {
    this.items = items;
    return this;
  }

  public List<CheckItemResult> getResults() {
    return results;
  }

  public CheckInventoryPayload setResults(List<CheckItemResult> results) {
    this.results = results;
    return this;
  }

  public CheckInventoryPayload addResult(CheckItemResult result) {
    if (results == null) {
      results = new ArrayList<>();
    }
    results.add(result);
    return this;
  }

  public boolean isFinished() {
    return finished;
  }

  public CheckInventoryPayload setFinished(boolean finished) {
    this.finished = finished;
    return this;
  }

  public CheckItemResult getMessage() {
    return message;
  }

  public CheckInventoryPayload setMessage(CheckItemResult message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return "CheckInventoryPayload{"
        + "checkId='"
        + checkId
        + '\''
        + ", sessionId='"
        + sessionId
        + '\''
        + ", items="
        + items
        + ", results="
        + results
        + ", finished="
        + finished
        + ", message="
        + message
        + '}';
  }
}
