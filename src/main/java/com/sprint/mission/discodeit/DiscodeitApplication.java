package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DiscodeitApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscodeitApplication.class, args);
  }
}
