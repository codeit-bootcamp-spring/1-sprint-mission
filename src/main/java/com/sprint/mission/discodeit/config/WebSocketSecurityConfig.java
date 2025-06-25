package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.socket.CustomAuthorizationChannelInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketSecurityConfig {

  private final AuthorizationManager<Message<?>> delegatingManager =
      MessageMatcherDelegatingAuthorizationManager.builder()
          .simpTypeMatchers(
              SimpMessageType.CONNECT,
              SimpMessageType.DISCONNECT,
              SimpMessageType.UNSUBSCRIBE
          ).permitAll()
          .simpDestMatchers("/pub/**").authenticated()
          .simpSubscribeDestMatchers("/sub/**").authenticated()
          .anyMessage().denyAll()
          .build();

  @Bean
  public AuthorizationManager<Message<?>> messageAuthorizationManager() {
    return (authentication, message) -> {
      AuthorizationResult authorize = delegatingManager.authorize(authentication,
          message);
      StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

      if (log.isDebugEnabled()) {
        log.debug("🚀 인가 결과: command={}, dest={}, user={}, granted={}",
            accessor.getCommand(),
            accessor.getDestination(),
            (authentication != null) ? authentication.get().getName() : "null",
            authorize != null && authorize.isGranted());
      }
      return delegatingManager.check(authentication, message);
    };
  }

  @Bean
  public CustomAuthorizationChannelInterceptor authorizationChannelInterceptor(
      AuthorizationManager<Message<?>> messageAuthorizationManager
  ) {
    return new CustomAuthorizationChannelInterceptor(messageAuthorizationManager);
  }

}
