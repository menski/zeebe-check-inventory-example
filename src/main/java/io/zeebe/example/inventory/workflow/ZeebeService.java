package io.zeebe.example.inventory.workflow;

import io.zeebe.client.ZeebeClient;
import org.springframework.stereotype.Service;

@Service
public class ZeebeService {

  private ZeebeClient zeebeClient = ZeebeClient.newClient();

  public ZeebeClient getClient() {
    return zeebeClient;
  }
}
