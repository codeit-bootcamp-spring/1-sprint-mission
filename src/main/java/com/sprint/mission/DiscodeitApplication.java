package com.sprint.mission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DiscodeitApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class,
        args);
    Environment env = context.getEnvironment();
    System.out.println("Repository Type: " + env.getProperty("discodeit.repository.type"));
    System.out.println("File Directory: " + env.getProperty("discodeit.repository.file-directory"));
  }
}
