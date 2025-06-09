package com.sprint.mission.discodeit.redis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@RequiredArgsConstructor
public class RedisRememberMeTokenRepository implements PersistentTokenRepository {

  private static final String TOKEN_KEY_PREFIX = "remember-me:";
  private static final String USER_KEY_PREFIX = "remember-me-user:";
  private static final int EXPIRE_TOKEN_TIME = 21;

  private final RedisTemplate<String, String> redis;
  private final RedisTemplate<String, String> userRedis;

  @Override
  public void createNewToken(PersistentRememberMeToken token) {
    String key = getKey(token);
    setToken(token, key);

    String userKey = getUserKey(token);
    setUserSet(token, userKey);
  }

  @Override
  public void updateToken(String series, String tokenValue, Date lastUsed) {
    String key = getKey(series);

    setToken(tokenValue, lastUsed, key);

    String username = String.valueOf(redis.opsForHash().get(key, "username"));
    String userKey = USER_KEY_PREFIX + username;
    userRedis.expire(userKey, EXPIRE_TOKEN_TIME, TimeUnit.DAYS);

  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(String seriesId) {
    String key = getKey(seriesId);

    List<Object> values = redis.opsForHash()
        .multiGet(key, List.of("username", "tokenValue", "lastUsed"));

    String username = (String) values.get(0);
    String tokenValue = (String) values.get(1);
    Date lastUsed = new Date(Long.parseLong((String) values.get(2)));

    return new PersistentRememberMeToken(username, seriesId, tokenValue, lastUsed);
  }

  @Override
  public void removeUserTokens(String username) {
    String userKey = getUserKey(username);

    Set<String> seriesSet = userRedis.opsForSet().members(userKey);
    if (seriesSet != null) {
      seriesSet.forEach(series -> redis.delete(getKey(series)));
    }

    userRedis.delete(userKey);
  }


  private String getUserKey(String username) {
    return "remember-me-user:" + username;
  }

  private String getKey(PersistentRememberMeToken token) {
    return TOKEN_KEY_PREFIX + token.getSeries();
  }

  private String getKey(String series) {
    return TOKEN_KEY_PREFIX + series;
  }

  private void setToken(PersistentRememberMeToken token, String key) {

    Map<String, String> data = Map.of(
        "username", token.getUsername(),
        "tokenValue", token.getTokenValue(),
        "lastUsed", String.valueOf(token.getDate().getTime())
    );

    redis.opsForHash().putAll(key, data);
    redis.expire(key, EXPIRE_TOKEN_TIME, TimeUnit.DAYS);
  }

  private void setToken(String tokenValue, Date lastUsed, String key) {
    redis.opsForHash().put(key, "tokenValue", tokenValue);
    redis.opsForHash().put(key, "lastUsed", String.valueOf(lastUsed.getTime()));
    redis.expire(key, EXPIRE_TOKEN_TIME, TimeUnit.DAYS);
  }

  private String getUserKey(PersistentRememberMeToken token) {
    return USER_KEY_PREFIX + token.getUsername();
  }

  private void setUserSet(PersistentRememberMeToken token, String userKey) {
    userRedis.opsForSet().add(userKey, token.getSeries());
    userRedis.expire(userKey, EXPIRE_TOKEN_TIME, TimeUnit.DAYS);
  }

}