package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
      ObjectMapper objectMapper) {
    ObjectMapper redisObjectMapper = objectMapper.copy();
    redisObjectMapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance,
        DefaultTyping.EVERYTHING,
        As.PROPERTY
    );

    // 기본 설정
    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper)))
        .prefixCacheNameWith("discodeit:")
        .entryTtl(Duration.ofMinutes(10))
        .disableCachingNullValues();

    // 캐시별 개별 설정
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    // 사용자 정보 - 1시간 (변경 빈도 낮음)
    cacheConfigurations.put(CacheNames.USERS, defaultConfig
        .entryTtl(Duration.ofHours(1)));

    // 사용자 채널 목록 - 30분 (중간 빈도)
    cacheConfigurations.put(CacheNames.USER_CHANNELS, defaultConfig
        .entryTtl(Duration.ofMinutes(30)));

    // 알림 - 5분 (실시간성 중요)
    cacheConfigurations.put(CacheNames.USER_NOTIFICATIONS, defaultConfig
        .entryTtl(Duration.ofMinutes(5)));

    // 읽음 상태 - 15분 (자주 변경됨)
    cacheConfigurations.put(CacheNames.USER_READ_STATUSES, defaultConfig
        .entryTtl(Duration.ofMinutes(15)));

    // 채널 참여자 - 2시간 (상대적으로 안정적)
    cacheConfigurations.put(CacheNames.CHANNEL_PARTICIPANTS, defaultConfig
        .entryTtl(Duration.ofHours(2)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

}

