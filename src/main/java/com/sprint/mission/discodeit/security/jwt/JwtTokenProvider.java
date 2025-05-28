package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.DiscodeitUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j // 로깅을 위한 log 필드 생성
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final JwtProperties jwtProperties;
  private final DiscodeitUserDetailsService userDetailsService; // ← 추가

  private final Clock clock = Clock.systemUTC(); // 현재 시각 제공 객체 > 테스트 시 모킹 가능

  @PostConstruct
  private void init() {
    validateSecretKey();  // 초기화 로직
  }

  /**
   * 액세스 토큰 생성
   */
  public String generateAccessToken(UserDetails user, UserDto userDto) {
    return generateToken(
        user,
        userDto,
        jwtProperties.getAccessToken().getValiditySeconds(),
        TokenType.ACCESS
    );
  }

  /**
   * 리프레시 토큰 생성
   */
  public String generateRefreshToken(UserDetails user, UserDto userDto) {
    return generateToken(
        user,
        userDto,
        jwtProperties.getRefreshToken().getValiditySeconds(),
        TokenType.REFRESH
    );
  }

  /**
   * 커스텀 클레임을 포함한 토큰 생성
   */
  public String generateTokenWithClaims(UserDetails user, Map<String, Object> extraClaims) {
    Instant now = clock.instant();
    Instant expiry = now.plusSeconds(jwtProperties.getAccessToken().getValiditySeconds());

    JwtBuilder builder = Jwts.builder()
        .header()
        .add("typ", "JWT")
        .and()
        .issuer(jwtProperties.getIssuer())
        .subject(user.getUsername())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiry))
        .id(UUID.randomUUID().toString()) // JTI
        .claim("type", TokenType.ACCESS.name())
        .claim("roles", extractRoles(user));

    // 커스텀 클레임 추가
    extraClaims.forEach(builder::claim);

    return builder
        .signWith(getSigningKey(), Jwts.SIG.HS256)
        .compact();
  }

  /**
   * 토큰 유효성(서명 및 만료 여부) 검증 (예외 없음)
   */
  public boolean validate(String token) {
    try {
      getClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.debug("JWT validation failed: {}", e.getMessage());
      return false;
    }
  }

  /**
   * 토큰에서 Authentication 객체 생성
   */
  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);

    String username = claims.getSubject();

    DiscodeitUserDetails principal = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(
        username);

    return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
  }

  /**
   * 토큰에서 클레임(내용부) 추출
   */
  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .clockSkewSeconds(60) // 시간오차 60초 허용
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * 토큰에서 사용자명(sub) 추출
   */
  public String getUsername(String token) {
    return getClaims(token).getSubject();
  }

  /**
   * 토큰 만료 시간(exp) 추출
   */
  public Date getExpiration(String token) {
    return getClaims(token).getExpiration();
  }

  /**
   * 토큰 만료 여부 확인
   */
  public boolean isTokenExpired(String token) {
    try {
      Date expiration = getExpiration(token);
      return expiration.before(Date.from(clock.instant()));
    } catch (JwtException e) {
      return true;
    }
  }

  // 이하 내부 유틸 메서드
  // 토큰 생성
  private String generateToken(UserDetails user, UserDto userDto, long validitySeconds,
      TokenType type) {
    Instant now = clock.instant();
    Instant expiry = now.plusSeconds(validitySeconds);

    return Jwts.builder()
        .header()
        .add("typ", "JWT")
        .and()
        .issuer(jwtProperties.getIssuer())
        .subject(user.getUsername())
        .issuedAt(Date.from(now)) // iat
        .expiration(Date.from(expiry)) // exp
        .id(UUID.randomUUID().toString())
        .claim("type", type.name())
        .claim("userDto", userDto) //userDto
        .claim("roles", extractRoles(user))
        .signWith(getSigningKey(), Jwts.SIG.HS256)
        .compact();
  }

  // 설정된 비밀 키를 HMAC 서명 키로 변환
  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // UserDetails 에서 권한 문자열 목록 추출
  private List<String> extractRoles(UserDetails user) {
    return user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
  }

  // 클레임에 포함된 roles 필드에서 권한 객체 목록 생성
  private Collection<SimpleGrantedAuthority> getAuthorities(Claims claims) {
    List<?> roles = claims.get("roles", List.class);
    if (roles == null) {
      return Collections.emptyList();
    }

    return roles.stream()
        .map(Object::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  // 시크릿 키의 최소 길이 검증 (32자 이상)
  private void validateSecretKey() {
    String secret = jwtProperties.getSecret();
    if (secret == null || secret.length() < 32) {
      throw new IllegalArgumentException(
          "JWT secret must be at least 32 characters long. Current length: " +
              (secret != null ? secret.length() : 0)
      );
    }
  }

  // 토큰 타입 열거형
  public enum TokenType {
    ACCESS, REFRESH
  }
}