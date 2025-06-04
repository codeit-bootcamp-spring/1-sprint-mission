package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtSessionRepository jwtSessionRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final JwtBlacklist jwtBlacklist;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(UserDto userDto, long expirationMs) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiration = Date.from(now.plusMillis(expirationMs));

        return Jwts.builder()
                .claim("userDto", userDto)
                .claim("type", "access")
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(UserDto userDto, long expirationMs) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiration = Date.from(now.plusMillis(expirationMs));

        return Jwts.builder()
                .claim("userDto", userDto)
                .claim("type", "refresh")
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Transactional
    public TokenPair generateTokenPair(User user) {
        jwtSessionRepository.deleteAllByUserId(user.getId());

        UserDto userDto = userMapper.toDto(user);
        String accessToken = generateAccessToken(userDto, accessTokenExpiration);
        String refreshToken = generateRefreshToken(userDto, refreshTokenExpiration);

        JwtSession session = JwtSession.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.ofInstant(Instant.now().plusMillis(refreshTokenExpiration),
                        ZoneId.systemDefault()))
                .build();

        jwtSessionRepository.save(session);

        return new TokenPair(accessToken, refreshToken);
    }

    //레프레시 토큰 활용한 엑세스 토큰 재발
    @Transactional
    public TokenPair reissueTokenPair(String oldRefreshToken) {
        JwtSession session = jwtSessionRepository.findByRefreshToken(oldRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (!validateToken(oldRefreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = session.getUser();
        UserDto userDto = userMapper.toDto(user);
        String newAccessToken = generateAccessToken(userDto, accessTokenExpiration);
        String newRefreshToken = generateRefreshToken(userDto, refreshTokenExpiration);

        session = JwtSession.builder()
                .id(session.getId())
                .user(user)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .createdAt(session.getCreatedAt())
                .expiresAt(LocalDateTime.ofInstant(Instant.now().plusMillis(refreshTokenExpiration),
                        ZoneId.systemDefault()))
                .build();

        jwtSessionRepository.save(session);

        return new TokenPair(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void invalidateRefreshToken(String refreshToken) {
        jwtSessionRepository.findByRefreshToken(refreshToken)
                .ifPresent(session -> {
                    Claims claims = getClaims(session.getAccessToken());
                    long exp = claims.getExpiration().getTime();
                    jwtBlacklist.blacklist(session.getAccessToken(), exp);

                    jwtSessionRepository.delete(session);
                });
    }

    public boolean validateToken(String token) {
        if (jwtBlacklist.blacklisted(token)) {
            log.warn("Blacklisted token");
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되었씁니다.: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("유효하지 않는 토큰입니다. : {}", e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Transactional(readOnly = true)
    public Optional<String> getAccessTokenByRefreshToken(String refreshToken) {
        return jwtSessionRepository.findByRefreshToken(refreshToken)
                .map(JwtSession::getAccessToken);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
