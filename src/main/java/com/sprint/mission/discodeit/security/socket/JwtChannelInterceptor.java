package com.sprint.mission.discodeit.security.socket;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    log.info("🚀 JwtChannelInterceptor 진입");
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);
    if (accessor != null && StompCommand.CONNECT.equals(
        accessor.getCommand())) {
      String authHeader = accessor.getFirstNativeHeader("Authorization");
      String token = null;
      if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
        token = authHeader.substring(7).trim();
      }
      if (token != null && jwtService.validateToken(token)) {
        String username = jwtService.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        log.info("🚀 JwtChannelInterceptor 인증 성공: {} | 권한: {}", auth.getName(),
            auth.getAuthorities());
        accessor.setUser(auth);
        Objects.requireNonNull(accessor.getSessionAttributes()).put("user", userDetails);
      } else {
        log.warn("🚀 JWT 토큰 검증 실패: 세션 ID {}", accessor.getSessionId());
        accessor.setHeader("error", "Invalid token");
        // todo: Exception
        throw new IllegalStateException("Invalid or Missing JWT Token");
      }
    }
    return message;
  }
}
