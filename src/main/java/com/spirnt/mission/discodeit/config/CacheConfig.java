package com.spirnt.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
class CacheConfig {

    // Caffeine

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users", "channels",
            "notifications");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES) // 쓰고 30분 후 만료
            .expireAfterAccess(10, TimeUnit.MINUTES)    // 무사용 10분 후 만료
            .maximumSize(5000)  // 최대 엔트리 수
            .recordStats()  // 통계 수집 활성화
            .removalListener((key, value, cause) -> // 제거 이벤트 리스너
                log.info("Cache removed: key={}, cause={}", key, cause));
    }

//    // Redis
//
//    /**
//     * GenericJackson2JsonRedisSerializer 사용 -> 다양한 타입의 객체를 별도의 타입 지정 없이 자동으로 JSON 직렬화/역직렬화
//     */
//    @Bean
//    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModules(
//            new JavaTimeModule(),   // Instant 등 Java8의 시간 타입 지원
//            new ParameterNamesModule(),  // 레코드 생성자 파라미터 인식(@JsonProperty 없는 경우)
//            new Jdk8Module()             // Optional 등 추가 타입 지원
//        );
//        objectMapper.disable(
//            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Instant를 ISO-8601 문자열 형식으로
//        objectMapper.enable(
//            MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES); // JSON 필드명 대소문자 유연한 매칭
//        objectMapper.disable(
//            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // 알 수 없는 필드는 그냥 무시 -> 예외 방지
//        // record
//        objectMapper.activateDefaultTyping(
//            // 검증기: 역직렬화 시 허용할 타입을 검증함
//            objectMapper.getPolymorphicTypeValidator(), // 기본 검증기: 역직렬화시 안전한 타입만 허용
//            DefaultTyping.EVERYTHING,  // 타입 정보를 추가함
//            JsonTypeInfo.As.PROPERTY    // 생성된 JSON에 타입 식별자 포함
//        );
//        return new GenericJackson2JsonRedisSerializer(objectMapper);
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
//            "host.docker.internal",
//            6379);
//        return new LettuceConnectionFactory(config);
//    }
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//            .serializeKeysWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(
//                    new StringRedisSerializer())
//            )
//            .serializeValuesWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(
//                    genericJackson2JsonRedisSerializer()
//                )
//            )
//            .entryTtl(Duration.ofMinutes(30))        // TTL 30분
//            .disableCachingNullValues();             // null 값 캐싱 금지
//    }
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
//        return RedisCacheManager.builder(factory) // RedisConnectionFactory로 캐시 매니저 구
//            .cacheDefaults(cacheConfiguration()) // 위에서 세팅한 기본 캐시 설정 적용
//            .transactionAware()                     // 트랜잭션 내 캐시 반영
//            .build(); // RedisCacheManager 인스턴스를 생성하고 등록해 줌
//    }
}
