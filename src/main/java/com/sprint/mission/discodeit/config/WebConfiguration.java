package com.sprint.mission.discodeit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

  private static final String[] loggingApi = {
      "/api/channels/**",
      "/api/messages/**",
      "/api/users/**",
      "/api/binaryContents/**"
  };

  private final ApiLoggingInterceptor apiLoggingInterceptor;
  private final MDCLoggingInterceptor mdcLoggingInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(apiLoggingInterceptor)
        .addPathPatterns(loggingApi);

    registry.addInterceptor(mdcLoggingInterceptor)
        .addPathPatterns("/**");
  }
}
