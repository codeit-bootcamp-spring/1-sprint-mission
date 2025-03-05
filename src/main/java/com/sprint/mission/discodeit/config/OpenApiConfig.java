package com.sprint.mission.discodeit.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("Discodeit API 문서")
        .description("Discodeit 프로젝트의 Swagger API 문서입니다.");

    List<Server> servers = List.of(new Server()
        .url("http://localhost:8080")
        .description("로컬 서버"));

    return new OpenAPI()
        .info(info)
        .servers(servers);
  }
}
