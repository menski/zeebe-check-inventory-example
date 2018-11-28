package io.zeebe.example.inventory.workflow;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckInventoryService {

  @Autowired private ItemStatusService itemStatus;

  @ZeebeWorker(type = "check-inventory")
  public void checkInventory(JobClient client, ActivatedJob job) {

    CheckInventoryPayload payload = job.getPayloadAsType(CheckInventoryPayload.class);
    payload.setCheckId(Long.toString(job.getHeaders().getWorkflowInstanceKey()));

    payload.getItems().forEach(item -> itemStatus.checkItemStatus(payload.getCheckId(), item));

    client.newCompleteCommand(job.getKey()).payload(payload).send().join();
  }
}
