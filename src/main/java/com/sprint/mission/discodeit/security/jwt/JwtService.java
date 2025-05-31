package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.security.InvalidRefreshTokenException;
import com.sprint.mission.discodeit.exception.security.JwtSessionNotFoundException;
import com.sprint.mission.discodeit.exception.security.MissingRefreshTokenException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtSessionRepository jwtSessionRepository;
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;
  private final BinaryContentMapper binaryContentMapper;

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.access-token-expiration}")
  private long accessTokenExpiration;

  @Value("${app.jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  private SecretKey signingKey;

  // 서명키
  private SecretKey getSigningKey() {
    if (this.signingKey == null) {
      byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
      this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }
    return signingKey;
  }

  /**
   * 토큰 생성및 세션 저장
   **/
  public JwtTokenDto generateTokensAndSaveSession(UserDto userDto) {
    if (userDto == null || userDto.getId() == null || userDto.getUsername() == null) {
      log.debug("userDto is null? : {}", userDto == null);
      log.debug("userDto.getId() is null? : {}", userDto.getId() == null);
      log.debug("userDto.getUsername() is null? : {}", userDto.getUsername() == null);
      throw new IllegalArgumentException("generateTokensAndSaveSession : userDto is illegal");
    }

    LocalDateTime currentTimeLdt = LocalDateTime.now();
    LocalDateTime accessTokenExpiresAtLdt =
        currentTimeLdt.plus(accessTokenExpiration, ChronoUnit.MILLIS);

    // Access Token 생성
    String accessToken = generateToken(userDto, currentTimeLdt, accessTokenExpiresAtLdt, "ACCESS");

    LocalDateTime refreshTokenExpiresAtLdt =
        currentTimeLdt.plus(refreshTokenExpiration, ChronoUnit.MILLIS);
    // Refresh Token 생성
    String refreshToken = generateToken(userDto, currentTimeLdt, refreshTokenExpiresAtLdt,
        "REFRESH");

    // JwtSession 저장
    JwtSession jwtSession = JwtSession.builder()
        .userId(userDto.getId())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .refreshTokenExpiresAt(refreshTokenExpiresAtLdt)
        .refreshTokenCreatedAt(currentTimeLdt)
        .revoked(false)
        .build();
    jwtSessionRepository.save(jwtSession);
    log.info("토큰 생성 및 세션 저장 완료");
    log.info("user : {}", userDto.getUsername());

    return new JwtTokenDto(accessToken, refreshToken);
  }

  // JwtTokenProvider 역할
  private String generateToken(UserDto userDto, LocalDateTime issuedAtLdt,
      LocalDateTime expiresAtLdt, String tokenType) {

    Date issuedAtDate = Date.from(issuedAtLdt.atZone(ZoneId.systemDefault()).toInstant());
    Date expiresAtDate = Date.from(expiresAtLdt.atZone(ZoneId.systemDefault()).toInstant());

//    List<String> roleNames = Collections.emptyList();
//    if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
//      roleNames = userDto.getRoles().stream().map(Role::getName).toList();
//    }

    String userDtoJsonString;
    try {
      userDtoJsonString = objectMapper.writeValueAsString(userDto);
    } catch (JsonProcessingException e) {
      log.error("UserDto를 Json으로 변환하는 과정에 오류 발생, userDto.getUsername={}", userDto.getUsername());
      throw new RuntimeException("UserDto를 Json으로 변환하는 과정에 오류 발생", e);
    }

    return Jwts.builder()
        .subject(userDto.getUsername())
        .claim("userDto", userDtoJsonString)
        .claim("type", tokenType)
        .issuedAt(issuedAtDate)
        .expiration(expiresAtDate)
        .signWith(getSigningKey()) // 서명
        .compact();
  }


  /**
   * Refresh Token 으로 Access Token 찾기
   **/
  public String getAccessTokenByRefreshToken(String refreshToken) {
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(
            () -> new JwtException("유효하지 않은 refreshToken으로 accessToken을 찾을 수 없습니다."));
    return jwtSession.getAccessToken();
  }


  /**
   * 토큰의 유효성 검증
   **/
  public boolean validateToken(String token) {
    if (token == null || token.trim().isEmpty()) {
      log.info("토큰 유효성 검증 실패 | token is null? : {}, token is empty? : {}", token == null,
          token.trim().isEmpty());
      return false;
    }
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      log.trace("토큰 유효성 검증 성공 | 토큰(10자 crop) : {}",
          token.substring(0, Math.min(token.length(), 10))); // 방어 코드
      // 나중에 블랙 리스트
      return true;
    } catch (ExpiredJwtException e) {
      log.info("토큰 유효성 검증 실패 | 만료된 토큰 : {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.info("토큰 유효성 검증 실패 | 잘못된 인자 존재 : {}", e.getMessage());
    } catch (JwtException e) {
      log.error("토큰 유효성 검증 실패 | JWT 관련 예외 : {}", e.getMessage());
    }
    return false;
  }

  /**
   * 유효한 Refresh Token 으로 새 Access Token 과 Refresh Token(Rotation) 발급
   **/
  @Transactional
  public Optional<JwtTokenDto> reissueTokenWithRotation(String oldRefreshToken) {
    // 토큰 유효성 검사 401
    if (oldRefreshToken == null || oldRefreshToken.trim().isEmpty()) {
      log.warn("RefreshToken | token is null? : {}, token is empty? : {}", oldRefreshToken == null,
          oldRefreshToken.trim().isEmpty());
      throw new MissingRefreshTokenException(Map.of("oldRefreshToken", oldRefreshToken));
    }
    if (!validateToken(oldRefreshToken)) {
      log.warn("리프레시 토큰이 유효하지 않습니다.");
      throw new InvalidRefreshTokenException(
          Map.of("oldRefreshToken", oldRefreshToken)); // TODO 나중에 리팩토링하면서 토큰 반환 다 지우기
    }

    // DB에서 oldRefreshToken를 가진 JwtSession 조회
    Optional<JwtSession> optionalJwtSession = jwtSessionRepository.findByRefreshToken(
        oldRefreshToken);
    if (optionalJwtSession.isEmpty()) {
      log.warn("RefreshToken을 DB에서 찾을 수 없습니다.");
      throw new JwtSessionNotFoundException(Map.of("oldRefreshToken", oldRefreshToken));
    }
    JwtSession oldSession = optionalJwtSession.get();

    // 세션 유효성 검사
    if (oldSession.isRevoked()) {
      log.warn("이미 취소된 토큰입니다.(id : {}) is Revoked? {}", oldSession.getId(), oldSession.isRevoked());
      return Optional.empty();
    }
    if (oldSession.isExpired()) {
      log.warn("만료된 토큰입니다.(id : {}) is expried? {}", oldSession.getId(), oldSession.isExpired());
      return Optional.empty();
    }

    // 새 토큰 발급을 위한 UsrDto 정보 준비
    Optional<UserDto> userDtoOptional = getUserDtoFromToken(oldRefreshToken);
    UserDto userDto = null;

    if (userDtoOptional.isPresent()) {
      userDto = userDtoOptional.get();
    } else {
      // 추출 실패시 userRepository 에서 직접 가져온다.
      log.warn("Token에서 UserDto를 추출하지 못해 User 정보 조회 로직으로 UserDto를 가져옵니다.");
      User user = userRepository.findById(oldSession.getUserId())
          .orElse(null);
      if (user == null) {
        log.error("DB 에서 유저를 찾지 못했습니다. 토큰을 삭제합니다.");
        jwtSessionRepository.delete(oldSession);
        return Optional.empty();
      }
      userDto = UserDto.builder()
          .username(user.getUsername())
          .email(user.getEmail())
          .profile(binaryContentMapper.toDto(user.getProfile()))
          .online(true)
          .roles(user.getRoles())
          .build();
    }

    // newAccessToken, newRefreshToken 생성
    LocalDateTime currentTimeLdt = LocalDateTime.now();
    LocalDateTime newAccessTokenExpiresAtLdt = currentTimeLdt.plus(accessTokenExpiration,
        ChronoUnit.MILLIS);
    String newAccessToken = generateToken(userDto, currentTimeLdt, newAccessTokenExpiresAtLdt,
        "ACCESS");
    LocalDateTime newRefreshTokenExpiresAtLdt = currentTimeLdt.plus(refreshTokenExpiration,
        ChronoUnit.MILLIS);
    String newRefreshToken = generateToken(userDto, currentTimeLdt, newRefreshTokenExpiresAtLdt,
        "REFRESH");

    // 이전 토큰 revoked, replacedBy 처리
    oldSession.updatedRevoked(true);
    oldSession.updatedReplacedBy(newRefreshToken);

    JwtSession newJwtSession = JwtSession.builder()
        .userId(userDto.getId())
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .refreshTokenExpiresAt(newRefreshTokenExpiresAtLdt)
        .refreshTokenCreatedAt(currentTimeLdt)
        .revoked(false)
        .build();

    jwtSessionRepository.save(newJwtSession);
    log.info("이전 JwtSession(id: {})  revoked : true 완료, 새로운 JwtSession (id: {}) 생성 완료",
        oldSession.getId(), newJwtSession.getId());

    return Optional.of(new JwtTokenDto(newAccessToken, newRefreshToken));
  }

  /**
   * Claims 객체에서 userDto 클레임 파싱하여 UserDto 객체로 변환
   **/
  public Optional<UserDto> getUserDtoFromToken(String token) {
    if (token == null || token.trim().isEmpty()) {
      return Optional.empty();
    }
    try {
      // 토큰 파싱 및 검증
      Claims claims = Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();

      // userDto 클레임 추출 및 역직렬화
      if (claims.containsKey("userDto")) {
        String userDtoJson = claims.get("userDto", String.class);
        try {
          UserDto userDto = objectMapper.readValue(userDtoJson, UserDto.class);
          return Optional.of(userDto);
        } catch (JsonProcessingException e) {
          log.error("userDtoJson userDto로 변환하는 과정에 오류 발생");
          return Optional.empty();
        }
      } else {
        log.warn("'userDto'클레임이 존재하지 않습니다.");
        return Optional.empty();
      }
    } catch (ExpiredJwtException e) {
      log.info("만료된 토큰에서는 userDto를 추출할 수 없습니다. : {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.info("잘못된 인자의 존재로 userDto를 추출할 수 없습니다. : {}", e.getMessage());
    } catch (JwtException e) {
      log.error("JWT 관련 예외로 userDto를 추출할 수 없습니다. : {}", e.getMessage());
    }
    return Optional.empty();
  }

  /**
   * Token 무효화
   **/
  public void revokeToken(UUID tokenId) {
    jwtSessionRepository.findById(tokenId)
        .ifPresent(token -> {
          token.updatedRevoked(true);
          jwtSessionRepository.save(token);
          log.debug("Token 무효화 성공: tokenId={}, user={}", tokenId, token.getId());
        });
  }

  public void revokeToken(String RefreshToken) {
    jwtSessionRepository.findByRefreshToken(RefreshToken)
        .ifPresent(token -> {
          token.updatedRevoked(true);
          jwtSessionRepository.save(token);
          log.info("Token 무효화 성공");
          log.debug("Token 무효화 성공: RefreshToken={}, user={}", RefreshToken, token.getId());
        });
  }
}