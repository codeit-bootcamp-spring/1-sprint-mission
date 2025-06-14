package com.sprint.mission.discodeit.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "discodeit.cache.type", havingValue = "local")
public class CaffeineCacheConfig {

	@Bean
	public Caffeine<Object, Object> caffeineSpec() {
		return Caffeine.newBuilder()
			.expireAfterAccess(10, TimeUnit.MINUTES)      // 무사용 10분 후 만료 (TTI)
			.expireAfterWrite(30, TimeUnit.MINUTES)       // 쓰기 후 30분 후 만료 (TTL)
			// .refreshAfterWrite(5, TimeUnit.MINUTES)       // 5분마다 백그라운드 갱신
			.maximumSize(5000)                            // 최대 5,000개 엔트리 유지 (LRU)
			.recordStats()                                // 통계 수집 활성화 (모니터링용)
			.removalListener((key, value, cause) ->       // 제거 이벤트 리스너
				log.info("Cache removed: key={}, cause={}", key, cause)
			);
	}

	@Bean
	public CacheManager caffeineCacheManager(Caffeine<Object, Object> caffeineSpec) {
		CaffeineCacheManager manager = new CaffeineCacheManager();
		manager.setCaffeine(caffeineSpec);
		return manager;
	}
}
