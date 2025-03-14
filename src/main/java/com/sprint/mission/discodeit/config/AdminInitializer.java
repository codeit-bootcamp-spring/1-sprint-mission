package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.service.AuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminInitializer{

  private final AuthService authService;

  @PostConstruct
  public void init() {
      authService.initAdmin();
  }
}
