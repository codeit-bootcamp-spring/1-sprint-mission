package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI codeItAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(info());
  }

  private Info info() {
    return new Info()
        .title("Discodeit API 문서")
        .description("Discodeit 프로젝트의 Swagger API 문서입니다.")
        .version("0.0.1");
  }
}
