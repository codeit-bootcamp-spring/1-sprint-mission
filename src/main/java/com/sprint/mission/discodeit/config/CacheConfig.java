package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
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
public class CacheConfig {

    public static final String USER_CHANNELS = "userChannels";
    public static final String USER_NOTIFICATIONS = "userNotifications";
    public static final String ALL_USERS = "allUsers";
    public static final String USER_DETAIL = "userDetail";
    public static final String CHANNEL_DETAIL = "channelDetail";

    @Value("${discodeit.cache.redis.ttl.user-channels:60}")
    private int userChannelsTtl;

    @Value("${discodeit.cache.redis.ttl.user-notifications:5}")
    private int userNotificationsTtl;

    @Value("${discodeit.cache.redis.ttl.all-users:10}")
    private int allUsersTtl;

    @Value("${discodeit.cache.redis.ttl.user-detail:30}")
    private int userDetailTtl;

    @Value("${discodeit.cache.redis.ttl.channel-detail:60}")
    private int channelDetailTtl;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
        ObjectMapper objectMapper) {
        ObjectMapper cacheObjectMapper = objectMapper.copy();
        cacheObjectMapper.findAndRegisterModules();

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .disableCachingNullValues()
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put(USER_CHANNELS, defaultConfig
            .entryTtl(Duration.ofMinutes(userChannelsTtl)));
        log.debug("사용자 채널 캐시 설정: TTL={}분", userChannelsTtl);

        cacheConfigurations.put(USER_NOTIFICATIONS, defaultConfig
            .entryTtl(Duration.ofMinutes(userNotificationsTtl)));
        log.debug("사용자 알림 캐시 설정: TTL={}분", userNotificationsTtl);

        cacheConfigurations.put(ALL_USERS, defaultConfig
            .entryTtl(Duration.ofMinutes(allUsersTtl)));
        log.debug("전체 사용자 캐시 설정: TTL={}분", allUsersTtl);

        cacheConfigurations.put(USER_DETAIL, defaultConfig
            .entryTtl(Duration.ofMinutes(userDetailTtl)));
        log.debug("사용자 상세 캐시 설정: TTL={}분", userDetailTtl);

        cacheConfigurations.put(CHANNEL_DETAIL, defaultConfig
            .entryTtl(Duration.ofMinutes(channelDetailTtl)));
        log.debug("채널 상세 캐시 설정: TTL={}분", channelDetailTtl);

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build();
    }

    /**
     * 커스텀 키 생성기 - 복합 키를 위한 설정
     */
    @Bean("customKeyGenerator")
    public KeyGenerator customKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append(target.getClass().getSimpleName())
                .append(".")
                .append(method.getName());

            for (Object param : params) {
                keyBuilder.append(":").append(param);
            }
            return keyBuilder.toString();
        };
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
        RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        ObjectMapper redisObjectMapper = objectMapper.copy();
        redisObjectMapper.findAndRegisterModules();

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper));

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper));

        template.afterPropertiesSet();
        log.debug("RedisTemplate 빈 생성 완료");
        return template;
    }
}
