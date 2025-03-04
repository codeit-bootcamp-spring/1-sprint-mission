package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Discodeit API 문서")
            .version("1.0")
            .description("Discodeit 프로젝트의 Swagger API 문서입니다."))
        .servers(List.of(
            new Server().url("http://localhost:8080").description("로컬 서버")
        ));
  }

  @Bean
  public GroupedOpenApi api() {
    String[] paths = {"/api/**"};
    String[] packagesToScan = {"com.sprint.mission.discodeit"};
    return GroupedOpenApi.builder().group("springdoc-openapi")
        .pathsToMatch(paths)
        .packagesToScan(packagesToScan).build();
  }

}
