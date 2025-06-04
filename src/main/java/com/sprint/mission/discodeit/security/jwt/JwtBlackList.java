package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtBlackList {

  private final Map<String, Instant> blackList = new ConcurrentHashMap<>();

  // 액세스 토큰 등록
  public void addTokenToBlackList(String accessToken, Instant expirationTime) {
    blackList.put(accessToken, expirationTime);
    log.info("블랙리스트에 액세스 토큰 추가 (만료 시각: {}): {}", expirationTime, accessToken);
  }

  // 액세스 토큰 블랙리스트에 존재하는지 확인
  public boolean isBlackListed(String accessToken) {
    Instant expiry = blackList.get(accessToken);
    return expiry != null && expiry.isAfter(Instant.now());
  }

  // 만료된 항목 제거
  @Scheduled(fixedDelay = 60 * 60 * 1000) // 1시간마다 실행
  public void cleanUpExpiredTokens() {
    Instant now = Instant.now();
    blackList.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    log.debug("블랙리스트 정리 완료");
  }

}
