package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableCaching
public class LocalCacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineSpec() {
        return Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)      // 무사용 10분 후 만료 (TTI)
            .expireAfterWrite(30, TimeUnit.MINUTES)       // 쓰기 후 30분 후 만료 (TTL)
            .maximumSize(5000)                                    // 최대 5,000개 엔트리 유지 (LRU)
            .recordStats()                                         // 통계 수집 활성화 (모니터링용)
            .removalListener((key, value, cause) ->   // 제거 이벤트 리스너(?)
                log.info("Cache removed: key={}, cause={}", key, cause)
            );
    }

    @Bean
    public CacheManager caffeineCacheManager(Caffeine<Object, Object> spec) {
        CaffeineCacheManager manager = new CaffeineCacheManager("users", "channels",
            "notifications");
        manager.setCaffeine(spec);
        return manager;
    }

    @Bean
    public KeyGenerator simpleKeyGen() {
        // 커스텀 KeyGenerator: 클래스명::메서드명::파라미터 합성
        return (target, method, params) -> {
            String base = target.getClass().getSimpleName() + ":" + method.getName();
            return base + ":" + Arrays.stream(params).map(Object::toString)
                .collect(Collectors.joining(","));
        };
    }
}

