package io.zeebe.example.inventory.app;

import io.zeebe.client.api.events.WorkflowInstanceEvent;
import io.zeebe.example.inventory.workflow.CheckInventoryPayload;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CheckItemsController {

  private Logger log = LoggerFactory.getLogger(CheckItemsController.class);

  @Autowired private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private ZeebeClientLifecycle zeebe;

  @MessageMapping("/checkItems")
  public void checkItems(CheckItemsRequest message, @Header("simpSessionId") String sessionId) {
    CheckInventoryPayload payload = new CheckInventoryPayload();
    payload.setSessionId(sessionId);
    payload.setItems(message.getItems());

    WorkflowInstanceEvent response =
        zeebe
            .workflowClient()
            .newCreateInstanceCommand()
            .bpmnProcessId("check-inventory")
            .latestVersion()
            .payload(payload)
            .send()
            .join();

    log.info("Created workflow for session {} instance: {}", sessionId, response);
  }
}
