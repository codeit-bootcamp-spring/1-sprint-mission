package com.sprint.mission.discodeit.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEvents {

  @EventListener
  public void onSuccess(AuthenticationSuccessEvent authenticationSuccessEvent) {
    log.info("로그인 성공: {}", authenticationSuccessEvent.getAuthentication().getName());
  }

  @EventListener
  public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
    log.error("로그인 실패: {}, {}", failureEvent.getAuthentication().getName(),
        failureEvent.getException().getMessage());
  }
}
