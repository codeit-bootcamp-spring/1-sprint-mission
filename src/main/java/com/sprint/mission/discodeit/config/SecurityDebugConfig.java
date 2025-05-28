package com.sprint.mission.discodeit.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.debug.DebugFilter;

@Configuration
public class SecurityDebugConfig {

  @Bean
  public FilterRegistrationBean<DebugFilter> debugFilter(FilterChainProxy filterChainProxy) {
    FilterRegistrationBean<DebugFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new DebugFilter(filterChainProxy));
    registration.addUrlPatterns("/*");
    registration.setOrder(Integer.MIN_VALUE);
    return registration;
  }
}