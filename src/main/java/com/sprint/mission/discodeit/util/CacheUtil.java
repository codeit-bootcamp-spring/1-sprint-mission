package com.sprint.mission.discodeit.util;


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;

@Slf4j
public class CacheUtil {

  public static void evictCache(CacheManager cacheManager, List<String> userIds, String cacheName) {
    log.info("[EVICTING Cache] : {}", cacheName);
    for (String userId : userIds) {
      cacheManager.getCache(cacheName).evict(userId);
    }
    log.info("[COMPLETED EVICTING] : {}", cacheName);
  }
}
