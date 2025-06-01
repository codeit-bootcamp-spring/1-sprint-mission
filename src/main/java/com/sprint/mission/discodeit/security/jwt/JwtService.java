package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.response.UserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

    private final JwtSessionRepository jwtSessionRepository;
    private final JwtProperties jwtProperties;
    private final Clock clock;

    @Autowired
    public JwtService(JwtSessionRepository jwtSessionRepository, JwtProperties jwtProperties) {
        this(jwtSessionRepository, jwtProperties, Clock.systemUTC());
    }

    // 테스트용 생성자 (Clock 주입 가능)
    public JwtService(JwtSessionRepository jwtSessionRepository, JwtProperties jwtProperties,
        Clock clock) {
        this.jwtSessionRepository = jwtSessionRepository;
        this.jwtProperties = jwtProperties;
        this.clock = clock;

        // 시작 시 비밀키 검증
        // validateSecretKey();
    }

    public String generateAccessToken(UserDetails user, UserResponse userResponse) {

        // 토큰 생성
        String accessToken = generateTokenWithClaims(user, Map.of("userDto", userResponse));

        return accessToken;
    }

    @Transactional
    public void saveJwtSession(UserResponse userResponse, String accessToken, String refreshToken) {
        // 만료 시간
        Date accessTokenExpiration = getExpiration(accessToken);
        Date refreshTokenExpiration = getExpiration(refreshToken);

        // JwtSession 저장
        JwtSession jwtSession = JwtSession.builder()
            .userId(userResponse.id())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiresAt(accessTokenExpiration.toInstant())
            .refreshTokenExpiresAt(refreshTokenExpiration.toInstant())
            .refreshCount(0)
            .build();

        jwtSessionRepository.save(jwtSession);
    }

    // 액세스 토큰 생성
    public String generateAccessToken(UserDetails user) {
        return generateToken(
            user,
            jwtProperties.getAccessToken().getValiditySeconds(),
            TokenType.ACCESS
        );
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(UserDetails user) {
        return generateToken(
            user,
            jwtProperties.getRefreshToken().getValiditySeconds(),
            TokenType.REFRESH
        );
    }

    // 만료시간 확인
    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // 커스텀 클레임을 포함한 토큰 생성
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

    // 토큰에서 클레임(내용) 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .clockSkewSeconds(60) // 오차범위
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("유효하지 않은 토큰입니다: {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 내부 유틸 메서드
     */
    // 토큰 생성 (액세스, 리프레시)
    private String generateToken(UserDetails user, long validitySeconds, TokenType type) {
        Instant now = clock.instant();
        Instant exp = now.plusSeconds(validitySeconds);

        return Jwts.builder()
            .header()
            .add("typ", "JWT")
            .and()
            .issuer(jwtProperties.getIssuer())
            .subject(user.getUsername())
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .id(UUID.randomUUID().toString())
            .claim("type", type.name())
            .claim("roles", extractRoles(user))
            .signWith(getSigningKey(), SIG.HS256)
            .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private List<String> extractRoles(UserDetails user) {
        return user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
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

    public enum TokenType {
        ACCESS, REFRESH
    }
}
