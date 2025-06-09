package com.sprint.mission.discodeit.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationEvents {

  @EventListener
  public void onFailure(AuthorizationDeniedEvent event) {
    log.error("인증 실패: {}", event.getAuthentication().get().getName());
  }
}
