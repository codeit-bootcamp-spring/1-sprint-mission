package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String token, long expirationMs) {
        blacklist.put(token, expirationMs);
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void cleanExpiredTokens() {
        long now = Instant.now().toEpochMilli();
        blacklist.entrySet().removeIf(entry -> now - entry.getValue() < now);
    }

    public boolean blacklisted(String token) {
        return blacklist.containsKey(token);
    }
}
