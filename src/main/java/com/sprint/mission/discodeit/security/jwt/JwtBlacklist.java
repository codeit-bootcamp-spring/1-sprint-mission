package com.sprint.mission.discodeit.security.jwt;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtBlacklist {

  private final ConcurrentHashMap<String, LocalDateTime> blackList = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  public JwtBlacklist() {
    log.info("토큰 삭제 스케줄링 실행");
    scheduler.scheduleAtFixedRate(() -> removeAllExpireToken(), 0, 30, TimeUnit.MINUTES);
  }

  public void put(String token, LocalDateTime expiration) {
    if (token == null || expiration == null) {
      throw new IllegalArgumentException();
    }

    blackList.put(token, expiration);
    log.info("토큰 블랙 리스트 추가 완료: {}, expire:{}", token, expiration);
  }

  public LocalDateTime get(String token) {
    checkNullToken(token);

    return blackList.get(token);
  }

  public boolean isBlocked(String token) {
    if (token == null || get(token) == null) {
      return false;
    }

    if (get(token).isBefore(LocalDateTime.now())) {
      log.info("만료된 access token 삭제: {}", token);
      remove(token);
      return false;
    }

    log.info("토큰이 블랙리스트에 존재합니다: {}", token);
    return true;
  }

  public void remove(String token) {
    checkNullToken(token);

    blackList.remove(token);
    log.info("토큰 삭제 완료:{}", token);
  }

  private void removeAllExpireToken() {
    log.info("만료 토큰 삭제 시도");
    LocalDateTime now = LocalDateTime.now();

    blackList.forEach((key, value) -> {
      if (value.isBefore(now)) {
        remove(key);
      }
    });
  }

  private void checkNullToken(String token) {
    if (token == null) {
      throw new IllegalArgumentException();
    }
  }
}
