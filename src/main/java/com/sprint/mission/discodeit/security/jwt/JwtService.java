package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.validator.UserValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtSessionRepository jwtSessionRepository;
    private final JwtProperties jwtProperties;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final JwtBlacklist jwtBlacklist;

    public void saveJwtSession(UserDto userDto, String accessToken, String refreshToken) {
        jwtSessionRepository.save(
            new JwtSession(userDto.id(), accessToken, refreshToken)
        );
    }

    public String getAccessToken(String refreshToken) {
        JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("유효하지 않은 refresh token"));

        return jwtSession.getAccessToken();
    }

    public void invalidateRefreshToken(String refreshToken) {
        JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("유효하지 않은 refresh token"));

        jwtSessionRepository.delete(jwtSession);

        String accessToken = jwtSession.getAccessToken();
        Date expirationTime = getExpiration(accessToken);
        jwtBlacklist.addAccessToken(accessToken, expirationTime);
    }

    @Transactional
    public String refresh(String refreshToken, UserDto userDto, HttpServletResponse response) {
        invalidateRefreshToken(refreshToken);

        String newAccessToken = generateAccessToken(userDto);
        String newRefreshToken = generateAccessToken(userDto);

        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/"); // 모든 경로에 쿠키 적용
        refreshTokenCookie.setMaxAge(
            (int) getExpiration(refreshToken).getTime()); // 초 단위
        response.addCookie(refreshTokenCookie);

        jwtSessionRepository.save(
            new JwtSession(userDto.id(), newAccessToken, newRefreshToken)
        );

        return newAccessToken;
    }

    /**
     * 액세스 토큰 생성
     */
    public String generateAccessToken(UserDto userDto) {
        return generateToken(
            userDto,
            jwtProperties.getAccessToken().getValiditySeconds(),
            TokenType.ACCESS
        );
    }

    /**
     * 리프레시 토큰 생성
     */
    public String generateRefreshToken(UserDto userDto) {
        return generateToken(
            userDto,
            jwtProperties.getRefreshToken().getValiditySeconds(),
            TokenType.REFRESH
        );
    }

    /**
     * 토큰 유효성(서명 및 만료 여부) 검증 (예외 없음)
     */
    public boolean validate(String token) {
        try {
            getClaims(token);
            return !jwtBlacklist.isBlacklisted(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 토큰에서 Authentication 객체 생성
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        String username = getUsername(token);
        User user = userValidator.validateUserExistsByUserName(username);
        UserDto userDto = userMapper.toUserDto(user, true);
        CustomUserDetails principal = new CustomUserDetails(
            userDto,
            user.getPassword()
        ); // 추후 수정
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
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
            return expiration.before(Date.from(Instant.now()));
        } catch (JwtException e) {
            return true;
        }
    }

    // 이하 내부 유틸 메서드
    // 토큰 생성
    private String generateToken(UserDto userDto, long validitySeconds, TokenType type) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(validitySeconds);

        return Jwts.builder()
            .header()
            .add("typ", "JWT")
            .and()
            .issuer(jwtProperties.getIssuer())
            .subject(userDto.username())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .id(UUID.randomUUID().toString())
            .claim("type", type.name())
            .claim("userDto", userDto)
            .claim("roles", extractRoles(userDto))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
    }

    // 설정된 비밀 키를 HMAC 서명 키로 변환
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // UserDetails 에서 권한 문자열 목록 추출
    private List<String> extractRoles(UserDto userDto) {
        return List.of(userDto.role().toString());
    }

    // 클레임에 포함된 roles 필드에서 권한 객체 목록 생성
    private Collection<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        List<?> roles = claims.get("roles", List.class);
        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
            .map(object -> "ROLE_".concat(object.toString())) // ROLE_ 접두사!!!
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
