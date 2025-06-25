package com.sprint.mission.discodeit.security.websocket;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

  private final JwtService jwtService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String token = extractToken(accessor);

      if (token != null && jwtService.validate(token)) {
        Authentication authentication = jwtService.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        accessor.setUser(authentication);
        log.debug("WebSocket 인증 성공: {}", authentication.getName());
      } else {
        log.warn("WebSocket 인증 실패: 유효하지 않은 토큰");
      }
    }

    return message;
  }

  private String extractToken(StompHeaderAccessor accessor) {
    return Optional.ofNullable(accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION))
        .filter(header -> header.startsWith("Bearer "))
        .map(header -> header.substring(7))
        .orElse(null);
  }
}
