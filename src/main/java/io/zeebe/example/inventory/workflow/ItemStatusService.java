package io.zeebe.example.inventory.workflow;

import io.zeebe.example.inventory.app.CheckItemResult;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemStatusService {

  @Autowired
  private ZeebeClientLifecycle zeebe;

  private Logger log = LoggerFactory.getLogger(ItemStatusService.class);
  private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
  private Random random = new Random();

  public int nextDelay() {
    return random.nextInt(50);
  }

  public boolean nextAvailability() {
    return random.nextBoolean();
  }

  public void checkItemStatus(String checkId, String item) {
    int delay = nextDelay();
    log.info("Scheduling item {} status for check id {} with delay {}ms", item, checkId, delay);

    executorService.schedule(
        () -> {
          CheckItemResult result =
              new CheckItemResult().setItem(item).setAvailable(nextAvailability());

          log.info(
              "Publishing item {} status message for check {} with available {}",
              item,
              checkId,
              result.isAvailable());

          zeebe
              .workflowClient()
              .newPublishMessageCommand()
              .messageName("item-status")
              .correlationKey(checkId)
              .payload(result)
              .timeToLive(Duration.ofMinutes(10))
              .send()
              .join();
        },
        delay,
        TimeUnit.MILLISECONDS);
  }
}
