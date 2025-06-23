package com.sprint.mission.discodeit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@RequiredArgsConstructor
public class WebSocketSecurityConfig {

  private final RoleHierarchy roleHierarchy;

  @Bean
  public AuthorizationManager<Message<?>> authorizationManager(RoleHierarchy roleHierarchy) {

    return MessageMatcherDelegatingAuthorizationManager.builder()
        .simpSubscribeDestMatchers("/sub/**").hasRole("USER")
        .simpMessageDestMatchers("/pub/**").hasAnyRole("USER", "ADMIN")
        .nullDestMatcher().permitAll()
        .anyMessage().denyAll()
        .build();
  }

  @Bean
  public ChannelInterceptor authorizationChannelInterceptor() {
    return new AuthorizationChannelInterceptor(authorizationManager(roleHierarchy));
  }
}
