//package com.sprint.mission.discodeit.configure;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
//import lombok.RequiredArgsConstructor;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@OpenAPIDefinition(
//    info = @Info(title = "타이틀",
//        description = "프로젝트 설명",
//        version = "v3"))
//@RequiredArgsConstructor
//@Configuration
//public class SwaggerConfig {
//
//  @Bean
//  public GroupedOpenApi chatOpenApi() {
//    String[] paths = {"/**"};
//
//    return GroupedOpenApi.builder()
//        .group("그룹명")
//        .pathsToMatch(paths)
//        .build();
//  }
//}
