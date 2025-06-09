package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

  @Value("${jwt.secret}")
  private String jwtSecret;

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public UserDto parseUserDto(String authorization) {

    String token = null;
    if (authorization != null && authorization.startsWith("Bearer ")) {
      token = authorization.substring(7);
    }

    Claims payload = Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Map<String, String> userDtoMap = payload.get("userDto", Map.class);
    return objectMapper.convertValue(userDtoMap, UserDto.class);
  }

  private SecretKey getSignKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }
}
