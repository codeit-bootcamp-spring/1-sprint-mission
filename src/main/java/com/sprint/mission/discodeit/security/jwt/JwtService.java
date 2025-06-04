package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtSessionRepository jwtSessionRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;
    private final JwtBlacklist jwtBlacklist;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration-seconds}")
    private long accessExpirationSeconds;

    @Value("${jwt.refresh-expiration-seconds}")
    private long refreshExpirationSeconds;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // 256-bit 이상 Base64 key
    }

    // UserDto로 Access/Refresh Token 생성 및 저장
    public String createToken(UserDto userDto) {
        Instant now = Instant.now();
        Instant accessExp = now.plusSeconds(accessExpirationSeconds);
        Instant refreshExp = now.plusSeconds(refreshExpirationSeconds); // 리프레시 토큰 만료 시간

        String jwt = Jwts.builder()
                .issuedAt(Date.from(now))                 // iat
                .expiration(Date.from(accessExp))         // Access Token 만료
                .claim("userDto", userDto)                // 사용자 정보
                .signWith(key, SignatureAlgorithm.HS256)  // 명시적 알고리즘 설정
                .compact();

        String refreshToken = UUID.randomUUID().toString();

        JwtSession session = JwtSession.builder()
                .refreshToken(refreshToken)
                .user(userRepository.getReferenceById(userDto.getId()))
                .accessToken(jwt)
                .issuedAt(now)
                .expiresAt(refreshExp) // 리프레시 토큰의 만료 시점 저장
                .revoked(false)
                .build();

        jwtSessionRepository.save(session);
        return jwt;
    }

    // 리프레시 토큰으로 재발급 (Rotation)
    public String refreshToken(String oldRefreshToken) {
        JwtSession session = jwtSessionRepository.findById(oldRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("세션 없음"));

        if (session.isRevoked() || session.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("리프레시 토큰이 만료되었거나 무효화됨");
        }

        session.revoke();  // Rotation: 기존 토큰 무효화
        jwtSessionRepository.save(session); // 수정됨

        UserDto userDto = userMapper.toDto(session.getUser());
        return createToken(userDto);  // 새 토큰 발급
    }

    // 토큰 유효성 검사 및 파싱
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 리프레시 토큰 무효화
    public void revoke(String refreshToken) {
        jwtSessionRepository.findById(refreshToken).ifPresent(session -> {
            session.revoke();
            jwtSessionRepository.save(session); // 무효화 후 저장

            // 심화요구사항 - 엑세스 토큰을 블랙리스트에 추가
            Instant exp = extractExpiration(session.getAccessToken());
            jwtBlacklist.add(session.getAccessToken(), exp);
        });
    }

    // 리프레시 토큰 삭제
    public void delete(String refreshToken) {
        jwtSessionRepository.deleteById(refreshToken);  // 삭제
    }


    public TokenPair issueTokenPair(UserDto userDto) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpirationSeconds);

        // 기존 세션 모두 무효화 - 심화 동시 로그인 제외
        jwtSessionRepository.updateRevokeAllByUserId(userDto.getId());

        String accessToken = Jwts.builder()
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("userDto", userDto)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = UUID.randomUUID().toString();

        JwtSession session = JwtSession.builder()
                .refreshToken(refreshToken)
                .user(userRepository.getReferenceById(userDto.getId()))
                .accessToken(accessToken)
                .issuedAt(now)
                .expiresAt(exp)
                .revoked(false)
                .build();

        jwtSessionRepository.save(session);

        return new TokenPair(accessToken, refreshToken);
    }

    public UserDto parseToken(String accessToken) {
        if (jwtBlacklist.contains(accessToken)) {
            throw new JwtException("블랙리스트에 등록된 토큰입니다.");
        }

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken) // 1. 유효성 및 서명 검증
                .getBody(); // 2. claims 추출

        Map<String, Object> map = (Map<String, Object>) claims.get("userDto");
        return objectMapper.convertValue(map, UserDto.class); // 3. DTO 변환
    }

    public String getAccessTokenByRefreshToken(String refreshToken) {
        JwtSession session = jwtSessionRepository.findById(refreshToken)
                .orElseThrow(() -> new AccessDeniedException("유효하지 않은 리프레시 토큰"));

        if (session.isRevoked() || session.getExpiresAt().isBefore(Instant.now())) {
            throw new AccessDeniedException("리프레시 토큰이 만료되었거나 무효화됨");
        }

        return session.getAccessToken();
    }

    public Instant extractExpiration(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().toInstant();
    }

    public JwtSession findSessionByAccessToken(String accessToken) {
        return jwtSessionRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new JwtException("세션이 존재하지 않습니다."));
    }

}