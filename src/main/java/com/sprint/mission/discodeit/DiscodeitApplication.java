package com.sprint.mission.discodeit;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class DiscodeitApplication {

  public static void main(String[] args) throws IOException {
    SpringApplication.run(DiscodeitApplication.class, args);
  }
}
