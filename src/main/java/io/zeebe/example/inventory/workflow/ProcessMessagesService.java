package io.zeebe.example.inventory.workflow;

import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.example.inventory.app.CheckItemResult;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessMessagesService {

  @Autowired
  public ResponseService responseService;

  private Logger log = LoggerFactory.getLogger(ProcessMessagesService.class);

  @ZeebeWorker(type = "process-messages")
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
      responseService.sendResponse(payload.getSessionId(), payload.getCheckId(), payload.getResults());
    }
  }

}
