package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JwtBlacklist {

  private static final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

  public static void addToBlacklist(String accessToken, Instant expirationTime) {
    blacklist.put(accessToken, expirationTime);
  }

  public static boolean isBlacklisted(String accessToken) {
    Instant expirationTime = blacklist.get(accessToken);

    if (expirationTime == null) {
      return false;
    }
    if (expirationTime.isBefore(Instant.now())) {
      blacklist.remove(accessToken);
      return false;
    }

    return true;
  }

}
