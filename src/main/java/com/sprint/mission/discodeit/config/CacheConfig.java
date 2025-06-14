package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public Caffeine<Object, Object> caffeineSpec() {
    return Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)      // 무사용 10분 후 만료 (TTI)
        .expireAfterWrite(30, TimeUnit.MINUTES)       // 쓰기 후 30분 후 만료 (TTL)
        .refreshAfterWrite(5, TimeUnit.MINUTES)       // 5분마다 백그라운드 갱신
        .maximumSize(5000)                            // 최대 5,000개 엔트리 유지 (LRU)
        .recordStats()                                // 통계 수집 활성화 (모니터링용)
        .removalListener((key, value, cause) ->       // 제거 이벤트 리스너
            log.info("Cache removed: key={}, cause={}", key, cause)
        );
  }

  @Bean
  public CacheManager caffeineCacheManager(Caffeine<Object, Object> spec) {
    CaffeineCacheManager manager = new CaffeineCacheManager(
        "userChannels",
        "userNotifications",
        "channelParticipants");
    manager.setCaffeine(spec);  // Caffeine 설정 적용
    return manager;
  }
}

