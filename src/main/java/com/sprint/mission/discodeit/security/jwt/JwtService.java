package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.jwt.JwtInvalidationException;
import com.sprint.mission.discodeit.exception.jwt.JwtTokenNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Getter
@Transactional
public class JwtService {

  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final JwtBlacklist jwtBlacklist;

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.accessTokenExpire}")
  private long accessTokenExpiration;

  @Value("${jwt.refreshTokenExpire}")
  private long refreshTokenExpiration;

  /**
   * @methodName : generateAccessToken
   * @date : 2025. 5. 27. 14:14
   * @author : wongil
   * @Description: jwt 토큰 생성
   **/
  public String generateAccessToken(UserDto dto) {
    validateUserDto(dto);

    Date now = new Date();
    Date expiration = new Date(now.getTime() + accessTokenExpiration);

    String accessToken = Jwts.builder()
        .header().add("typ", "JWT")
        .and()
        .subject(dto.username())
        .claim("userDto", dto)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(getSignKey())
        .compact();

    User user = userRepository.findUserByEmail(dto.email());
    JwtSession jwtSession = new JwtSession(accessToken, generateRefreshToken(dto), user);
    jwtSessionRepository.save(jwtSession);

    log.info("jwt 토큰 발급 성공: {}", accessToken);
    return accessToken;
  }


  /**
   * @methodName : generateRefreshToken
   * @date : 2025. 5. 27. 14:14
   * @author : wongil
   * @Description: Refresh 토큰 생성
   **/
  public String generateRefreshToken(UserDto dto) {
    validateUserDto(dto);

    Date now = new Date();
    Date expiration = new Date(now.getTime() + refreshTokenExpiration);

    log.info("Refresh 토큰 발급 성공");
    return Jwts.builder()
        .subject(dto.username())
        .claim("type", "REFRESH")
        .claim("userDto", dto)
        .issuedAt(now)
        .signWith(getSignKey())
        .expiration(expiration)
        .compact();
  }

  /**
   * @methodName : validate
   * @date : 2025. 5. 27. 14:22
   * @author : wongil
   * @Description: 토큰 검증
   **/
  public boolean validate(String token) {
    if (token == null) {
      return false;
    }

    if (jwtBlacklist.isBlocked(token)) {
      return false;
    }

    try {
      Jwts.parser()
          .verifyWith(getSignKey())
          .build()
          .parseSignedClaims(token);

      return true;
    } catch (ExpiredJwtException e) {
      log.info("토큰 만료:{}", token);
      invalidAccessToken(token);
      SecurityContextHolder.clearContext();
      return false;
    } catch (Exception e) {
      log.warn("토큰 검증 실패: {}", token);
      throw new JwtInvalidationException(Instant.now(), ErrorCode.JWT_INVALID, Map.of(
          ErrorCode.JWT_INVALID.getCode(),
          ErrorCode.JWT_INVALID.getMessage()
      ));
    }
  }

  /**
   * @methodName : reIssue
   * @date : 2025. 5. 27. 14:44
   * @author : wongil
   * @Description: refresh 토큰을 이용해서 jwt token 재발급
   **/
  public JwtSession reIssue(String token) {
    log.info("access token 재발급 시도");
    checkToken(token);

    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(token)
        .orElseThrow(
            () -> new JwtTokenNotFoundException(Instant.now(), ErrorCode.NOT_FOUND_JWT, Map.of(
                ErrorCode.NOT_FOUND_JWT.getCode(),
                ErrorCode.NOT_FOUND_JWT.getMessage()
            )));

    String refreshToken = jwtSession.getRefreshToken();
    if (!validate(refreshToken)) {
      log.warn("유효하지 않는 토큰: {}", refreshToken);
      throw new JwtInvalidationException(Instant.now(), ErrorCode.JWT_INVALID, Map.of(
          ErrorCode.JWT_INVALID.getCode(),
          ErrorCode.JWT_INVALID.getMessage()
      ));
    }

    jwtSessionRepository.deleteAllByRefreshToken(refreshToken);
    log.info("기존 refresh 토큰 삭제: {}", refreshToken);

//    UserDto userDto = getUserDto(refreshToken);
    User user = jwtSession.getUser();
    User newUser = userRepository.findById(user.getId())
        .orElseThrow(
            () -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of(
                ErrorCode.USER_STATUS_NOT_FOUND.getCode(),
                ErrorCode.USER_STATUS_NOT_FOUND.getMessage()
            )));

    BinaryContentDto binaryContentDto;
    if (user.getProfile() == null) {
      binaryContentDto = null;
    } else {
      binaryContentDto = new BinaryContentDto(newUser.getProfile().getId(),
          newUser.getProfile().getFileName(),
          newUser.getProfile().getSize(), newUser.getProfile().getContentType());
    }

    UserDto userDto = UserDto.builder()
        .id(newUser.getId())
        .username(newUser.getUsername())
        .email(newUser.getEmail())
        .profile(binaryContentDto)
        .online(jwtSessionRepository.existsByUser_Id(newUser.getId()))
        .Role(newUser.getRole())
        .build();

    String accessToken = createAccessToken(userDto);
    log.info("새로운 access 토큰 생성: {}", accessToken);

    String newRefreshToken = generateRefreshToken(userDto);
    log.info("새로운 refresh 토큰 생성: {}", newRefreshToken);

    JwtSession newSession = new JwtSession(accessToken, newRefreshToken, user);
    jwtSessionRepository.save(newSession);

    return newSession;
  }

  /**
   * @methodName : invalidToken
   * @date : 2025. 5. 27. 17:42
   * @author : wongil
   * @Description: 토큰 무효화
   **/
  public void invalidRefreshToken(String token) {
    if (token != null) {
      jwtBlacklist.put(token, getExpire(token));
      log.info("토큰 블랙 리스트 추가 완료:{}", token);

      jwtSessionRepository.deleteAllByRefreshToken(token);
      log.info("토큰 무효: {}", token);
    }
  }

  public void invalidAccessToken(String token) {
    if (token != null) {

      LocalDateTime expire;
      try {
        expire = getExpire(token);
      } catch (ExpiredJwtException e) {
        expire = e.getClaims().getExpiration()
            .toInstant()
            .atZone(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime();
      }

      jwtBlacklist.put(token, expire);
      log.info("토큰 블랙 리스트 추가 완료:{}", token);

      jwtSessionRepository.deleteAllByAccessToken(token);
      log.info("토큰 무효: {}", token);
    }
  }

  public void invalidTokenByUserId(UUID userId) {
    if (userId != null && jwtSessionRepository.existsByUser_Id(userId)) {

      List<JwtSession> jwtTokens = jwtSessionRepository.findAllByUser_Id(userId);
      jwtTokens.forEach(
          token -> {
            jwtBlacklist.put(token.getAccessToken(), getExpire(token.getAccessToken()));
            log.info("토큰 블랙 리스트 추가 완료:{}", token);
          });

      jwtSessionRepository.deleteAllByUser_Id(userId);
      log.info("유저의 모든 토큰 무효: {}", userId);
    }
  }

  /**
   * @methodName : createAuthentication
   * @date : 2025. 5. 27. 18:09
   * @author : wongil
   * @Description: jwt token으로부터 인증 객체 생성
   **/
  public Authentication createAuthentication(String token) {

    ObjectMapper objectMapper = new ObjectMapper();

    Claims payload = Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Map<String, Object> userDto = payload.get("userDto", Map.class);
    UserDto dto = objectMapper.convertValue(userDto, UserDto.class);

    log.info("Jwt Authentication 생성");
    return new UsernamePasswordAuthenticationToken(dto, token,
        List.of(new SimpleGrantedAuthority(dto.Role().toString())));
  }

  private void checkToken(String tokenId) {
    if (tokenId == null) {
      throw new IllegalArgumentException();
    }
  }

  public LocalDateTime getExpire(String token) {
    Date expiration = Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration();

    return expiration.toInstant()
        .atZone(ZoneId.of("Asia/Seoul"))
        .toLocalDateTime();
  }

  public String createAccessToken(UserDto userDto) {
    validateUserDto(userDto);

    Date now = new Date();
    Date expiration = new Date(now.getTime() + accessTokenExpiration);

    log.info("access token 생성");
    return Jwts.builder()
        .header().add("typ", "JWT")
        .and()
        .subject(userDto.username())
        .claim("userDto", userDto)
        .signWith(getSignKey())
        .issuedAt(now)
        .expiration(expiration)
        .compact();
  }

  private UserDto getUserDto(String token) {
    Claims payload = Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Map<String, Object> userMap = payload.get("userDto", Map.class);

    return objectMapper.convertValue(userMap, UserDto.class);
  }


  private SecretKey getSignKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  private void validateUserDto(UserDto dto) {
    if (dto == null) {
      throw new IllegalArgumentException();
    }
  }
}
