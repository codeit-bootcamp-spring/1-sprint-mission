package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.config.CacheConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 캐시 성능 모니터링 컨트롤러 (관리자용)
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/cache")
@Tag(name = "Cache Management", description = "캐시 관리 API (어드민 전용)")
@PreAuthorize("hasRole('ADMIN')")
public class CacheMetricsController {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConnectionFactory redisConnectionFactory;

    @Operation(summary = "전체 캐시 통계 조회")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("userChannels", getCacheStatistics(CacheConfig.USER_CHANNELS));
        stats.put("userNotifications", getCacheStatistics(CacheConfig.USER_NOTIFICATIONS));
        stats.put("allUsers", getCacheStatistics(CacheConfig.ALL_USERS));
        stats.put("userDetail", getCacheStatistics(CacheConfig.USER_DETAIL));
        stats.put("channelDetail", getCacheStatistics(CacheConfig.CHANNEL_DETAIL));

        stats.put("redisInfo", getRedisInfo());

        log.info("캐시 통계 조회 완료");
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "특정 캐시 통계 조회")
    @GetMapping("/stats/{cacheName}")
    public ResponseEntity<Map<String, Object>> getCacheStatsByName(@PathVariable String cacheName) {
        Map<String, Object> stats = getCacheStatistics(cacheName);
        if (stats.isEmpty()) {
            log.warn("존재하지 않는 캐시: {}", cacheName);
            return ResponseEntity.notFound().build();
        }

        log.info("캐시 통계 조회 완료: {}", cacheName);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "전체 캐시 클리어")
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        Map<String, String> result = new HashMap<>();

        clearCache(CacheConfig.USER_CHANNELS);
        clearCache(CacheConfig.USER_NOTIFICATIONS);
        clearCache(CacheConfig.ALL_USERS);
        clearCache(CacheConfig.USER_DETAIL);
        clearCache(CacheConfig.CHANNEL_DETAIL);

        result.put("message", "모든 캐시가 클리어되었습니다.");
        log.info("모든 캐시 클리어 완료");
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "특정 캐시 클리어")
    @DeleteMapping("/clear/{cacheName}")
    public ResponseEntity<Map<String, String>> clearCacheByName(@PathVariable String cacheName) {
        Map<String, String> result = new HashMap<>();

        if (clearCache(cacheName)) {
            result.put("message", cacheName + " 캐시가 클리어되었습니다.");
            log.info("캐시 클리어 완료: {}", cacheName);
            return ResponseEntity.ok(result);
        } else {
            result.put("error", "존재하지 않는 캐시: " + cacheName);
            log.warn("존재하지 않는 캐시 클리어 시도: {}", cacheName);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "캐시 키 목록 조회")
    @GetMapping("/keys/{cacheName}")
    public ResponseEntity<Map<String, Object>> getCacheKeys(@PathVariable String cacheName) {
        Cache springCache = cacheManager.getCache(cacheName);
        if (springCache == null) {
            log.warn("존재하지 않는 캐시: {}", cacheName);
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        if (springCache instanceof RedisCache) {
            String keyPattern = "discodeit:cache:" + cacheName + ":*";
            Set<String> keys = redisTemplate.keys(keyPattern);

            result.put("cacheName", cacheName);
            result.put("keys", keys);
            result.put("size", keys != null ? keys.size() : 0);
            result.put("keyPattern", keyPattern);
        }

        log.info("캐시 키 목록 조회 완료: {}", cacheName);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Redis 서버 정보 조회")
    @GetMapping("/redis/info")
    public ResponseEntity<Map<String, Object>> getRedisServerInfo() {
        Map<String, Object> result = new HashMap<>();

        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            result.put("redisInfo", connection.info().toString());
            result.put("dbSize", connection.dbSize());
            result.put("lastSave", connection.lastSave());
        } catch (Exception e) {
            log.error("Redis 정보 조회 실패", e);
            result.put("error", "Redis 정보 조회 실패: " + e.getMessage());
        }

        return ResponseEntity.ok(result);
    }

    /**
     * 캐시 통계 정보를 조회
     */
    private Map<String, Object> getCacheStatistics(String cacheName) {
        Map<String, Object> stats = new HashMap<>();
        Cache springCache = cacheManager.getCache(cacheName);

        if (springCache instanceof RedisCache) {
            String keyPattern = "discodeit:cache:" + cacheName + ":*";
            Set<String> keys = redisTemplate.keys(keyPattern);
            long cacheSize = keys != null ? keys.size() : 0;

            stats.put("cacheName", cacheName);
            stats.put("size", cacheSize);
            stats.put("status", cacheSize > 0 ? "ACTIVE" : "EMPTY");
            stats.put("keyPattern", keyPattern);
        }

        return stats;
    }

    private boolean clearCache(String cacheName) {
        Cache springCache = cacheManager.getCache(cacheName);
        if (springCache != null) {
            springCache.clear();
            return true;
        }
        return false;
    }

    /**
     * Redis 기본 정보 조회
     */
    private Map<String, Object> getRedisInfo() {
        Map<String, Object> redisInfo = new HashMap<>();

        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            redisInfo.put("dbSize", connection.dbSize());
            redisInfo.put("connected", true);

            // 전체 캐시 키 패턴으로 캐시된 항목 수 조회
            Set<String> allCacheKeys = redisTemplate.keys("discodeit:cache:*");
            redisInfo.put("totalCacheKeys", allCacheKeys != null ? allCacheKeys.size() : 0);

        } catch (Exception e) {
            log.error("Redis 정보 조회 실패", e);
            redisInfo.put("connected", false);
            redisInfo.put("error", e.getMessage());
        }

        return redisInfo;
    }
}
