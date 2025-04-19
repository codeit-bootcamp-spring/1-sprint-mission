package com.sprint.mission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
//@ConfigurationPropertiesScan
public class DiscodeitApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscodeitApplication.class, args);
  }
}
