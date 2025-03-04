package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Discodeit API",
        version = "1.0.0",
        description = "스프린트 미션 5 API"
    )
)
public class OpenAPIConfig {

  @Bean
  public GroupedOpenApi apiGroup() {
    return GroupedOpenApi.builder()
        .group("all")
        .pathsToMatch("/api/**")
        .build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI();
  }
}