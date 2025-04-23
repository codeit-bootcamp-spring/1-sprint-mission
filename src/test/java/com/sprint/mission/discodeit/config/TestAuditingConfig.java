package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class TestAuditingConfig {

  @Bean
  public AuditorAware<UUID> auditorProvider() {
    return () -> Optional.of(UUID.randomUUID());
  }
}
