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

    public static final String USER_CHANNELS = "userChannels";
    public static final String USER_NOTIFICATIONS = "userNotifications";
    public static final String ALL_USERS = "allUsers";
    public static final String USER_DETAIL = "userDetail";
    public static final String CHANNEL_DETAIL = "channelDetail";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        //기본 캐시 설정
        cacheManager.setCaffeine(caffeineCacheBuilder());

        //캐시별 설정
        cacheManager.registerCustomCache(USER_CHANNELS, userChannelsCacheBuilder().build());
        cacheManager.registerCustomCache(USER_NOTIFICATIONS,
            userNotificationsCacheBuilder().build());
        cacheManager.registerCustomCache(ALL_USERS, allUsersCacheBuilder().build());
        cacheManager.registerCustomCache(USER_DETAIL, userDetailCacheBuilder().build());
        cacheManager.registerCustomCache(CHANNEL_DETAIL, channelDetailCacheBuilder().build());

        return cacheManager;
    }

    /**
     * 기본 캐시 설정
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .recordStats();
    }

    /**
     * 사용자별 채널 목록 캐시 설정 - 자주 조회되므로 긴 TTL 설정 - 채널 변경이 빈번하지 않음
     */
    private Caffeine<Object, Object> userChannelsCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(50)
            .maximumSize(500)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .recordStats()
            .evictionListener((key, value, cause) ->
                log.debug("사용자 채널 캐시 제거: key={}, cause={}", key, cause));
    }


    /**
     * 사용자별 알림 목록 캐시 설정 - 실시간성 중요하므로 짧은 TTL 설정
     */
    private Caffeine<Object, Object> userNotificationsCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(50)
            .maximumSize(300)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
            .evictionListener((key, value, cause) ->
                log.debug("사용자 알림 캐시 제거: key={}, cause={}", key, cause));
    }

    /**
     * 사용자 목록 캐시 설정 - 전체 목록이므로 적당한 TTL 설정
     */
    private Caffeine<Object, Object> allUsersCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(10)
            .maximumSize(20)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .recordStats()
            .evictionListener((key, value, cause) ->
                log.debug("전체 사용자 캐시 제거: key={}, cause={}", key, cause));
    }

    /**
     * 사용자 상세 정보 캐시 설정
     */
    private Caffeine<Object, Object> userDetailCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .recordStats()
            .evictionListener((key, value, cause) ->
                log.debug("사용자 상세 캐시 제거: key={}, cause={}", key, cause));
    }


    /**
     * 채널 상세 정보 캐시 설정
     */
    private Caffeine<Object, Object> channelDetailCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(50)
            .maximumSize(500)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .recordStats()
            .evictionListener((key, value, cause) ->
                log.debug("채널 상세 캐시 제거: key={}, cause={}", key, cause));
    }
}
