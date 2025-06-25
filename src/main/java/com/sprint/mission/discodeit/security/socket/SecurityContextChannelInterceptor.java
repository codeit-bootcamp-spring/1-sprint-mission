package com.sprint.mission.discodeit.security.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityContextChannelInterceptor implements ChannelInterceptor {

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    log.info("🚀 SecurityContextChannelInterceptor 접근");
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    if (accessor.getSessionId() != null) {
      if (accessor.getUser() instanceof Authentication auth) {
        SecurityContext originalContext = SecurityContextHolder.getContext();
        try {
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          context.setAuthentication(auth);
          SecurityContextHolder.setContext(context);
          log.debug("🚀 SecurityContextChannel 설정 완료: {}", auth.getName());
        } catch (Exception e) {
          SecurityContextHolder.setContext(originalContext);
          log.error("SecurityContext 설정 실패", e);
        }
      }
    }
    return message;
  }
}
