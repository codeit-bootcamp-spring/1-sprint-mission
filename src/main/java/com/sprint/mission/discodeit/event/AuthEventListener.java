package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventListener {

  private final JwtService jwtService;

  @EventListener
  @Async
  public void handleUserAuthorityChanged(UserRoleChangedEvent event) {
    String username = event.username();

    log.info("권한 변경된 사용자 세션 무효화 시작: username = {}", username);

    jwtService.invalidateRefreshToken(username);
  }
}
