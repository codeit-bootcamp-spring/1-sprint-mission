package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.security.jwt.JwtHeader;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscodeitChannelInterceptor implements ChannelInterceptor {

  private final JwtService jwtService;
  private final JwtUtils jwtUtils;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      List<String> authHeaders = accessor.getNativeHeader(JwtHeader.JWT_HEADER);
      if (authHeaders != null && !authHeaders.isEmpty()) {
        String authorization = authHeaders.get(0);
        if (authorization != null && authorization.startsWith("Bearer ")) {
          String token = authorization.substring(7);

          Authentication authentication = jwtService.createAuthentication(token);
          SecurityContextHolder.getContext().setAuthentication(authentication);
          accessor.setUser(authentication);
        }
      }
    }

    return message;
  }
}
