package com.sprint.mission.discodeit.security.jwt;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtBlacklist {

    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    // 블랙리스트에 토큰 추가
    public void add(String accessToken, Instant expiresAt) {
        blacklist.put(accessToken, expiresAt);
    }

    // 블랙리스트에 토큰이 있는지 확인
    public boolean contains(String accessToken) {
        Instant expiresAt = blacklist.get(accessToken);
        if (expiresAt == null) return false;

        // 만료된 경우 자동 제거
        if (expiresAt.isBefore(Instant.now())) {
            blacklist.remove(accessToken);
            return false;
        }

        return true;
    }

}