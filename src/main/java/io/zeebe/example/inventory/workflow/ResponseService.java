package io.zeebe.example.inventory.workflow;

import io.zeebe.example.inventory.app.CheckItemResult;
import io.zeebe.example.inventory.app.CheckItemsResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

  @Autowired
  public SimpMessagingTemplate messagingTemplate;

  public void sendResponse(String sessionId, String checkId, List<CheckItemResult> results) {
    messagingTemplate.convertAndSendToUser(
      sessionId,
      "/queue/results",
      new CheckItemsResponse().setCheckId(checkId).setItems(results),
      messageHeaders(sessionId));
  }

  private Map<String, Object> messageHeaders(String sessionId) {
    HashMap<String, Object> headers = new HashMap<>();
    headers.put("simpSessionId", sessionId);
    headers.put(
      "nativeHeaders",
      Collections.singletonMap("destination", Collections.singletonList("/app/checkItems")));
    return headers;
  }
}
