package io.zeebe.example.inventory.workflow;

import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.subscription.JobWorker;
import io.zeebe.example.inventory.app.CheckItemResult;
import io.zeebe.example.inventory.app.CheckItemsResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProcessMessagesService {

  @Autowired public ZeebeService zeebe;

  @Autowired public SimpMessagingTemplate messagingTemplate;

  private Logger log = LoggerFactory.getLogger(ProcessMessagesService.class);
  private JobWorker worker;

  @PostConstruct
  public void openWorker() {
    worker =
        zeebe
            .getClient()
            .jobClient()
            .newWorker()
            .jobType("process-messages")
            .handler(this::processMessages)
            .pollInterval(Duration.ofMillis(10))
            .open();
  }

  @PreDestroy
  public void closeWorker() {
    if (worker != null) {
      worker.close();
      worker = null;
    }
  }

  public void processMessages(JobClient client, ActivatedJob job) {
    CheckInventoryPayload payload = job.getPayloadAsType(CheckInventoryPayload.class);

    // process message
    CheckItemResult message = payload.getMessage();
    log.info("Processing message {}", message);
    payload.addResult(message);

    // check if all messages arrived
    payload.setFinished(payload.getResults().size() >= payload.getItems().size());

    log.info("Processing finished {}", payload);

    client.newCompleteCommand(job.getKey()).payload(payload).send().join();

    if (payload.isFinished()) {
      sendResponse(payload.getSessionId(), payload.getResults());
    }
  }

  private void sendResponse(String sessionId, List<CheckItemResult> results) {
    messagingTemplate.convertAndSendToUser(
        sessionId,
        "/queue/results",
        new CheckItemsResponse().setItems(results),
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
