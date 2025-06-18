package com.sprint.mission.discodeit.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheStatsLogger {

  private final CacheManager cacheManager;

  public void logStats(String cacheName) {
    if (cacheManager.getCache(cacheName) instanceof CaffeineCache caffeineCache) {
      log.info("☕ [{}] Caffeine Stats: {}", cacheName, caffeineCache.getNativeCache().stats());
    } else {
      log.warn("☕ Cache '{}' is not a Caffeine cache or not found.", cacheName);
    }
  }

}
