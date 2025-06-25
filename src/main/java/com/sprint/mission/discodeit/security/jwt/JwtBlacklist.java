package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

  private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

  public void put(String accessToken, Instant expirationTime) {
    blacklist.putIfAbsent(accessToken, expirationTime);
  }

  public boolean contains(String accessToken) {
    return blacklist.containsKey(accessToken);
  }

  // 1시간마다 정리
  @Scheduled(fixedDelay = 60 * 60 * 1000)
  public void cleanUp() {
    blacklist.values().removeIf(expirationTime -> expirationTime.isBefore(Instant.now()));
  }
}