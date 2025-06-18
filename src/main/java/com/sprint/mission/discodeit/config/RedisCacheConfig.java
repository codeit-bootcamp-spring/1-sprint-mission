package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableCaching
public class RedisCacheConfig {

  private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);
  private static final Duration ALL_USERS_TTL = Duration.ofMinutes(1);
  private static final Set<String> CACHE_NAMES = Set.of(
      "allUsers",
      "userChannels",
      "userNotifications"
  );

  // 기본 캐시 설정
  @Bean
  public RedisCacheConfiguration defaultCacheConfig(ObjectMapper objectMapper) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(DEFAULT_TTL)
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(createSerializer(objectMapper)))
        .disableCachingNullValues();
  }

  // allUsers 전용 캐시 설정
  @Bean
  public RedisCacheConfiguration allUsersCacheConfig(ObjectMapper objectMapper) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(ALL_USERS_TTL)
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(createSerializer(objectMapper)))
        .disableCachingNullValues();
  }

  // 공통 직렬화 설정
  private GenericJackson2JsonRedisSerializer createSerializer(ObjectMapper objectMapper) {
    log.info("✅ Redis Serializer uses ObjectMapper with modules: {}",
        objectMapper.getRegisteredModuleIds());
    return new GenericJackson2JsonRedisSerializer(objectMapper);
  }

  // 캐시 매니저 설정
  @Bean
  public CacheManager cacheManager(
      RedisConnectionFactory redisConnectionFactory,
      @Qualifier("defaultCacheConfig") RedisCacheConfiguration defaultConfig,
      @Qualifier("allUsersCacheConfig") RedisCacheConfiguration allUsersConfig
  ) {
    Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
    configMap.put("allUsers", allUsersConfig);
    configMap.put("userChannels", defaultConfig);
    configMap.put("userNotifications", defaultConfig);

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(defaultConfig)
        .initialCacheNames(CACHE_NAMES)
        .withInitialCacheConfigurations(configMap)
        .build();
  }

  // RedisTemplate 설정
  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory connectionFactory,
      ObjectMapper objectMapper
  ) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(createSerializer(objectMapper));
    return template;
  }

}
