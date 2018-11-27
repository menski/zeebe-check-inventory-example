package io.zeebe.example.inventory;

import io.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
public class Application extends SpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
