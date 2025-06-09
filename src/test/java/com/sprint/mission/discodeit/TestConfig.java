package com.sprint.mission.discodeit;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@TestConfiguration
public class TestConfig {

  @Bean
  @Primary
  public S3AsyncClient s3AsyncClient() {
    return Mockito.mock(S3AsyncClient.class);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(a -> a.anyRequest().permitAll());
    return http.build();
  }
}
