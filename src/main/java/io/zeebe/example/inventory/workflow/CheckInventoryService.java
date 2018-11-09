package io.zeebe.example.inventory.workflow;

import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.subscription.JobWorker;
import java.time.Duration;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckInventoryService {

  @Autowired private ZeebeService zeebe;

  @Autowired private ItemStatusService itemStatus;

  private JobWorker worker;

  @PostConstruct
  public void openWorker() {
    worker =
        zeebe
            .getClient()
            .jobClient()
            .newWorker()
            .jobType("check-inventory")
            .handler(this::checkInventory)
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

  public void checkInventory(JobClient client, ActivatedJob job) {
    CheckInventoryPayload payload = job.getPayloadAsType(CheckInventoryPayload.class);
    payload.setCheckId(Long.toString(job.getHeaders().getWorkflowInstanceKey()));

    payload.getItems().forEach(item -> itemStatus.checkItemStatus(payload.getCheckId(), item));

    client.newCompleteCommand(job.getKey()).payload(payload).send().join();
  }
}
