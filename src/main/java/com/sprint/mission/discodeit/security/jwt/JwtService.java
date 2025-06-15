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
import org.springframework.security.authentication.BadCredentialsException;
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
    private final JwtBlacklist jwtBlacklist;

    @Autowired
    public JwtService(JwtSessionRepository jwtSessionRepository, JwtProperties jwtProperties,
        JwtBlacklist jwtBlacklist) {
        this(jwtSessionRepository, jwtProperties, Clock.systemUTC(), jwtBlacklist);
    }

    // 테스트용 생성자 (Clock 주입 가능)
    public JwtService(JwtSessionRepository jwtSessionRepository, JwtProperties jwtProperties,
        Clock clock, JwtBlacklist jwtBlacklist) {
        this.jwtSessionRepository = jwtSessionRepository;
        this.jwtProperties = jwtProperties;
        this.clock = clock;
        this.jwtBlacklist = jwtBlacklist;

        // 시작 시 비밀키 검증
        // validateSecretKey();
    }

    public String generateAccessToken(UserDetails user, UserResponse userResponse) {
        return generateTokenWithClaims(user, Map.of("userDto", userResponse));
    }

    @Transactional
    public void saveJwtSession(UserResponse userResponse, String accessToken, String refreshToken) {

        // 동시 로그인 제한
        List<JwtSession> activeSessions = jwtSessionRepository.findActiveSessionsByUserId(
            userResponse.id(), Instant.now());
        if (!activeSessions.isEmpty()) {
            activeSessions.forEach(JwtSession::revoke);
        }

        Date accessTokenExpiration = getExpiration(accessToken);
        Date refreshTokenExpiration = getExpiration(refreshToken);

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

    @Transactional
    public void updateJwtSession(JwtSession jwtSession, String accessToken, String refreshToken) {
        Date accessTokenExpiration = getExpiration(accessToken);
        Date refreshTokenExpiration = getExpiration(refreshToken);

        JwtSession getJwtSession = findJwtSessionByRefreshToken(jwtSession.getRefreshToken());

        getJwtSession.updateAccessToken(accessToken);
        getJwtSession.updateRefreshToken(refreshToken);
        getJwtSession.updateAccessTokenExpiresAt(accessTokenExpiration.toInstant());
        getJwtSession.updateAccessTokenExpiresAt(refreshTokenExpiration.toInstant());
        getJwtSession.incrementRefreshCount();
        jwtSessionRepository.save(getJwtSession);
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
            Claims claims = getClaims(token);

            String tokenId = claims.getId();
            if (jwtBlacklist.isBlacklisted(tokenId)) {
                log.warn("블랙리스트에 포함된 토큰: {}", tokenId);
                return false;
            }
            return true;

        } catch (ExpiredJwtException e) {
            log.info("토큰이 만료되었습니다: {}", e.getMessage());
            throw new BadCredentialsException("유효하지 않은 토큰입니다");

        } catch (JwtException e) {
            log.info("유효하지 않은 토큰입니다: {}", e.getMessage());
            throw new BadCredentialsException("유효하지 않은 토큰입니다");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public JwtSession findJwtSessionByRefreshToken(String refreshToken) {
        // 없는 경우
        JwtSession jwtSession = findByRefreshTokenOrThrow(refreshToken);

        // 만료된 경우
        if (!jwtSession.isRefreshTokenValid()) {
            throw new BadCredentialsException("토큰이 만료되었습니다.");
        }
        return jwtSession;
    }

    public void revokeRefreshToken(String refreshToken) {
        JwtSession jwtSession = findByRefreshTokenOrThrow(refreshToken);
        jwtSession.revoke();

        Claims claims = getClaims(jwtSession.getAccessToken());
        String tokenId = claims.getId();
        Instant expiryTime = claims.getExpiration().toInstant();
        jwtBlacklist.addToBlacklist(tokenId, expiryTime);

        jwtSessionRepository.save(jwtSession);
    }

    @Transactional
    public void revokeAllUserSessions(UUID userId) {
        jwtSessionRepository.revokeAllSessionsByUserId(userId);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }


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

    private JwtSession findByRefreshTokenOrThrow(String refreshToken) {
        return jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new BadCredentialsException("유효하지 않은 토큰입니다."));
    }

    public enum TokenType {
        ACCESS, REFRESH
    }
}
