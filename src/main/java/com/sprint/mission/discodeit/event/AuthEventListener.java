package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventListener {

  private final SessionRegistry sessionRegistry;
  private final JwtService jwtService;

  @EventListener
  @Async
  public void handleUserAuthorityChanged(UserRoleChangedEvent event) {
    String username = event.username();

    log.info("권한 변경된 사용자 세션 무효화 시작: username = {}", username);

    // 1. 모든 로그인된 사용자의 세션 조회
    // 현재 Discord 서비스에 로그인한 모든 사용자들의 Principal 객체들을 가져온다.
    List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

    // 2. 권한이 변경된 사용자 찾기
    // SessionRegistry가 "사용자 검색" 기능까지 가지면 책임이 너무 많아지기 때문에, 구현되어있지 않음.
    // 따라서 모든 principal을 순회하면서 찾아야한다. --> 대용량 서비스에서는 Redis를 사용하는 이유.
    for (Object principal : allPrincipals) {
      if (principal instanceof DiscodeitUserDetails userDetails) {

        if (username.equals(userDetails.getUsername())) {
          log.info("대상 사용자 발견: username = {}", username);

          List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);

          for (SessionInformation session : sessions) {
            log.info("세션 무효화: sessionId = {}", session.getSessionId());
            session.expireNow();
          }
          log.info("사용자의 모든 세션 무효화 완료: username = {}, 무효화된 세션 수 = {}",
              username, sessions.size());
        }
      }
    }
    jwtService.invalidateRefreshToken(username);
    //강제 로그아웃 하더라도 해당 유저가 새로고침하지 않는다면,
    // 엑세스 토큰의 유효기간 동안은 이전의 권한으로 여전히 API 요청을 할 수 있습니다.
    // 이 버그에 대해서는 심화 요구사항을 수행하면서 해결합니다.
  }

}
