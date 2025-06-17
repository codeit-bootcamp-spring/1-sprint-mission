package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration(ObjectMapper objectMapper) {
    ObjectMapper redisObjectMapper = objectMapper.copy(); //Jackson ObjectMapper를 복사해서 Redis 전용으로 사용
    redisObjectMapper.activateDefaultTyping( //객체 타입 정보를 JSON에 포함시켜 역직렬화시 원본 타입으로 복원 가능
        LaissezFaireSubTypeValidator.instance, //Jackson 라이브러리의 타입 검증자
        DefaultTyping.EVERYTHING,
        As.PROPERTY
    );

    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                // GenericJackson2JsonRedisSerializer로 JSON 형태로 저장(직렬화)
                new GenericJackson2JsonRedisSerializer(redisObjectMapper)
            )
        )
        .prefixCacheNameWith("discodeit:") // 모든 캐시 키에 "discodeit:" 접두사 추가
        .entryTtl(Duration.ofSeconds(600)) // 600초(10분) 후 자동 만료
        .disableCachingNullValues(); //null 값은 캐싱하지 않음
  }
}

