package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory connectionFactory,
      ObjectMapper objectMapper) {

    Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
        Object.class);

    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(serializer));

    Map<String, RedisCacheConfiguration> ttlConfig = new HashMap<>();
    ttlConfig.put("users", config.entryTtl(Duration.ofSeconds(5)));
    ttlConfig.put("channels", config.entryTtl(Duration.ofSeconds(5)));
    ttlConfig.put("readStatuses", config.entryTtl(Duration.ofSeconds(5)));
    ttlConfig.put("noti", config.entryTtl(Duration.ofSeconds(3)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(config)
        .withInitialCacheConfigurations(ttlConfig)
        .build();
  }
}

// Caffeine cache
//  @Bean
//  public Caffeine<Object, Object> caffeine() {
//    return Caffeine.newBuilder()
//        .expireAfterAccess(5, TimeUnit.SECONDS)
//        .expireAfterWrite(10, TimeUnit.SECONDS)
//        .maximumSize(5000)
//        .removalListener((key, value, cause) ->
//            log.info("[캐시 제거] key:{}, value:{}, cause:{}", key, value, cause)
//        );
//  }
//
//  @Bean
//  public CacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
//    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("users", "channels",
//        "readStatuses", "noti");
//    caffeineCacheManager.setCaffeine(caffeine);
//
//    return caffeineCacheManager;
//  }
