package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidRefreshTokenException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${jwt.secret-key}")
  private String jwtSecret;
  @Value("${jwt.access-token-validity}")
  private Duration accessTokenValidity;
  @Value("${jwt.refresh-token-validity}")
  private Duration refreshTokenValidity;

  private final UserMapper userMapper;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;

  protected SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(UserDto userDto) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + accessTokenValidity.toMillis());

    return Jwts.builder()
        .setSubject("accessToken")
        .claim("userDto", userDto)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(getSecretKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(UserDto userDto) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + refreshTokenValidity.toMillis());

    return Jwts.builder()
        .setSubject("refreshToken")
        .claim("userId", userDto.id())
        .setIssuedAt(new Date())
        .setExpiration(expiry)
        .signWith(getSecretKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Transactional
  public void saveJwtSession(UserDto userDto, String refreshToken) {
    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> new RuntimeException("User not found"));

    JwtSession jwtSession = new JwtSession();
    jwtSession.setUser(user);
    jwtSession.setRefreshToken(refreshToken);
    jwtSession.setRevoked(false);
    jwtSession.setExpiresAt(LocalDateTime.now().plus(refreshTokenValidity));
    jwtSessionRepository.save(jwtSession);
  }

  public boolean isValidRefreshToken(String token) {
    try {
      Claims claims = parseClaims(token);
      return "refreshToken".equals(claims.getSubject());
    } catch (Exception e) {
      return false;
    }
  }


  public boolean isValidAccessToken(String token) {
    try {
      Claims claims = parseClaims(token);
      return claims.getSubject().equals("accessToken");
    } catch (Exception e) {
      return false;
    }
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }


  @Transactional
  public String refreshAccessToken(String refreshToken) {
    JwtSession session = getSessionByRefresh(refreshToken)
        .orElseThrow(InvalidRefreshTokenException::expiredOrInvalid);

    UserDto userDto = userMapper.toDto(session.getUser());
    return generateAccessToken(userDto);
  }


  public Optional<JwtSession> getSessionByRefresh(String refreshToken) {
    return jwtSessionRepository.findByRefreshTokenAndRevokedFalse(refreshToken);
  }


  public void revokeRefreshToken(String refreshToken) {
    Optional<JwtSession> session = jwtSessionRepository.findByRefreshToken(refreshToken);
    session.ifPresent(jwtSession -> {
      jwtSession.setRevoked(true);
      jwtSessionRepository.save(jwtSession);
    });
  }

  @Transactional
  public void forceLogoutByUserId(UUID userId) {
    jwtSessionRepository.deleteAllByUserId(userId);
  }

  public void removeTokenCookies(HttpServletResponse response) {
    deleteCookie(response, "refreshToken");
    deleteCookie(response, "accessToken");
  }

  private void deleteCookie(HttpServletResponse response, String name) {
    ResponseCookie cookie = ResponseCookie.from(name, null)
        .httpOnly(false)
        .secure(false)
        .path("/")
        .maxAge(Duration.ZERO)
        .build();
    response.addHeader("Set-Cookie", cookie.toString());
  }
  public void handleLoginSuccess(UserDto userDto, HttpServletResponse response) throws IOException {
    String accessToken = generateAccessToken(userDto);
    String refreshToken = generateRefreshToken(userDto);
    saveJwtSession(userDto, refreshToken);

    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
        .httpOnly(true)
        .secure(false)
        .path("/")
        .maxAge(refreshTokenValidity)
        .build();
    response.addHeader("Set-Cookie", refreshCookie.toString());

    // Optional: 응답 바디 설정
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(accessToken);
  }

}

