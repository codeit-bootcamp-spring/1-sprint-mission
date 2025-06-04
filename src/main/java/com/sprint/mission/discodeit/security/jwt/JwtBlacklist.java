package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

    private final Map<String, Date> tokenBlacklist = new ConcurrentHashMap<>();

    public void addAccessToken(String token, Date expirationTime) {
        tokenBlacklist.put(token, expirationTime);
    }

    public boolean isBlacklisted(String token) {
        Date expiration = tokenBlacklist.get(token);
        if (expiration == null) {
            return false;
        }
        if (expiration.before(Date.from(Instant.now()))) {
            tokenBlacklist.remove(token);
            return false;
        }
        return true;
    }

    // 메모리 누수 방지를 위한 주기적 정리
    @Scheduled(fixedRate = 60 * 60 * 1000) // 매 1시간마다 실행
    public void cleanupExpiredTokens() {
        Date now = new Date();
        tokenBlacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}
