package com.sprint.mission.discodeit.config;

/*
@Slf4j
@Configuration
@EnableCaching
public class CaffeineCacheConfig {

  @Bean
  public CacheManager caffeineCacheManager(Caffeine<Object, Object> spec) {
    CaffeineCacheManager manager = new CaffeineCacheManager("userChannels", "userNotifications",
        "allUsers");
    manager.setCaffeine(spec);
    return manager;
  }

  @Bean
  public Caffeine<Object, Object> caffeineSpec() {
    return Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .expireAfterWrite(30, TimeUnit.MINUTES)
//        .refreshAfterWrite(5, TimeUnit.MINUTES)
        .maximumSize(5000)
        .recordStats()
        .removalListener((key, value, cause) -> {
          log.info("Cache Removed: key={}, cause={}", key, cause);
        });
  }
}
*/