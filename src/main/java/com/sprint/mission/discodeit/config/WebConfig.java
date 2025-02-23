package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.interceptor.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestLoggingInterceptor())
        .order(1)
        .addPathPatterns("/**");
  }
}
