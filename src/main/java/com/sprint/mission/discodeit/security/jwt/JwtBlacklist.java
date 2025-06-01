package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

    private final ConcurrentHashMap<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public JwtBlacklist() {
        // 1시간마다 만료된 토큰 정리하기
        scheduler.scheduleAtFixedRate(this::removeExpiredTokens, 1, 1, TimeUnit.HOURS);
    }

    public void addToBlacklist(String tokenId, Instant expiryTime) {
        blacklistedTokens.put(tokenId, expiryTime);
    }

    public boolean isBlacklisted(String tokenId) {
        return blacklistedTokens.containsKey(tokenId);
    }

    private void removeExpiredTokens() {
        Instant now = Instant.now();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
