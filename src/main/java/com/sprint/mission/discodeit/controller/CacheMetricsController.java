package com.sprint.mission.discodeit.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.sprint.mission.discodeit.config.CacheConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
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

    @Operation(summary = "전체 캐시 통계 조회")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("userChannels", getCacheStatistics(CacheConfig.USER_CHANNELS));
        stats.put("userNotifications", getCacheStatistics(CacheConfig.USER_NOTIFICATIONS));
        stats.put("allUsers", getCacheStatistics(CacheConfig.ALL_USERS));
        stats.put("userDetail", getCacheStatistics(CacheConfig.USER_DETAIL));
        stats.put("channelDetail", getCacheStatistics(CacheConfig.CHANNEL_DETAIL));

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
        if (springCache instanceof CaffeineCache caffeineCache) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                caffeineCache.getNativeCache();
            result.put("cacheName", cacheName);
            result.put("keys", nativeCache.asMap().keySet());
            result.put("size", nativeCache.estimatedSize());
        }

        log.info("캐시 키 목록 조회 완료: {}", cacheName);
        return ResponseEntity.ok(result);
    }

    /**
     * 캐시 통계 정보를 조회
     */
    private Map<String, Object> getCacheStatistics(String cacheName) {
        Map<String, Object> stats = new HashMap<>();
        Cache springCache = cacheManager.getCache(cacheName);

        if (springCache instanceof CaffeineCache caffeineCache) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                caffeineCache.getNativeCache();
            CacheStats cacheStats = nativeCache.stats();

            long requestCount = cacheStats.requestCount();

            stats.put("cacheName", cacheName);
            stats.put("estimatedSize", nativeCache.estimatedSize());
            stats.put("requestCount", requestCount);
            stats.put("hitCount", cacheStats.hitCount());

            if (requestCount == 0) {
                stats.put("hitRate", "N/A (요청 없음)");
                stats.put("missRate", "N/A (요청 없음)");
                stats.put("status", "UNUSED");
            } else {
                double hitRate = (double) cacheStats.hitCount() / requestCount * 100;
                double missRate = (double) cacheStats.missCount() / requestCount * 100;
                stats.put("hitRate", String.format("%.2f%%", hitRate));
                stats.put("missRate", String.format("%.2f%%", missRate));
                stats.put("status", "ACTIVE");
            }

            stats.put("missCount", cacheStats.missCount());
            stats.put("loadCount", cacheStats.loadCount());
            stats.put("evictionCount", cacheStats.evictionCount());
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
}
