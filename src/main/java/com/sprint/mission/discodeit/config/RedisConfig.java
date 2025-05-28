package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.redis.RedisRememberMeTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

@Configuration
@RequiredArgsConstructor
@EnableRedisIndexedHttpSession(maxInactiveIntervalInSeconds = 900)
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {

  @Value("${spring.data.redis.password}")
  private String password;

  @Bean
  LettuceConnectionFactory redisConnectionFactory() {

    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setPassword(password);

    return new LettuceConnectionFactory(config);
  }

  @Bean
  RedisTemplate<String, PersistentRememberMeToken> redisTemplate(
      LettuceConnectionFactory lettuceConnectionFactory) {

    RedisTemplate<String, PersistentRememberMeToken> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(lettuceConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    redisTemplate.afterPropertiesSet();

    return redisTemplate;
  }

  @Bean
  RedisRememberMeTokenRepository redisTokenRepository(
      RedisTemplate<String, String> redisTemplate,
      RedisTemplate<String, String> redisUserTemplate
  ) {

    return new RedisRememberMeTokenRepository(redisTemplate, redisUserTemplate);
  }

}
