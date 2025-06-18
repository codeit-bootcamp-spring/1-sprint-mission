package com.spirnt.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.DiscodeitException;
import com.spirnt.mission.discodeit.exception.ErrorCode;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.exception.auth.InvalidJwtTokenException;
import com.spirnt.mission.discodeit.exception.auth.JwtSessionNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtSessionRepository jwtSessionRepository;
    private final UserRepository userRepository;
    private final JwtBlacklist jwtBlacklist;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    public enum TokenType {
        ACCESS,
        REFRESH
    }

    @Transactional
    public JwtSession createJwtSession(UserDto userDto) {
        JwtObject accessToken = generateAccessToken(userDto);
        JwtObject refreshToken = generateRefreshToken(userDto);
        JwtSession jwtSession = new JwtSession(userDto.getId(), accessToken.token(),
            refreshToken.token(), accessToken.expirationTime());
        jwtSessionRepository.save(jwtSession);

        return jwtSession;
    }


    // 토큰 유효성 검증
    public boolean isValid(String token) {
        boolean verified;
        try {
            // 토큰이 시크릿을 이용해 올바르게 서명되었는가?
            JWSVerifier verifier = new MACVerifier(jwtProperties.getSecret());
            JWSObject jwsObject = JWSObject.parse(token);
            verified = jwsObject.verify(verifier);

            // 토큰 형태가 올바른가? 토큰이 만료되지 않았는가?
            if (verified) {
                JwtObject jwtObject = parse(token);
                verified = !jwtObject.isExpired();
            }
            // 블랙 리스트에 있는 액세스 토큰인가?
            if (verified) {
                verified = !jwtBlacklist.existsInBlacklist(token);
            }
        } catch (JOSEException | ParseException e) {
            verified = false;
        }
        return verified;
    }

    // 리프레시 토큰을 이용해 새로운 토큰 발급
    // 액세스, 리프레시 모두 새로 발급하는 rotatation 전
    @Transactional
    public JwtSession rotateToken(String refreshToken) {
        // 토큰 유효성 검증
        if (refreshToken == null || !isValid(refreshToken)) {
            throw new InvalidJwtTokenException(Map.of());
        }

        JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new JwtSessionNotFoundException(Map.of()));

        // 기존 액세스 토큰을 블랙리스트에 추가
        jwtBlacklist.addBlacklist(jwtSession.getAccessToken(), jwtSession.getExpirationTime());

        // 토큰 재발급
        UUID userId = jwtSession.getUserId();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
        UserDto userDto = userMapper.toDto(user);
        JwtObject newAccessToken = generateAccessToken(userDto);
        JwtObject newRefreshToken = generateRefreshToken(userDto);
        // Rotation 전략
        jwtSession.update(newAccessToken.token(), newRefreshToken.token(),
            newAccessToken.expirationTime());

        return jwtSession;
    }

    // 토큰 무효화
    @Transactional
    public void invalidateToken(String refreshToken) {
        JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new JwtSessionNotFoundException(Map.of()));

        // 액세스 토큰을 블랙리스트에 추가
        jwtBlacklist.addBlacklist(jwtSession.getAccessToken(), jwtSession.getExpirationTime());

        jwtSessionRepository.delete(jwtSession);
    }

    @Transactional
    public void deleteJwtSession(UUID userId) {
        jwtSessionRepository.findByUserId(userId)
            .ifPresent(this::invalidate);
    }

    private void invalidate(JwtSession jwtSession) {
        jwtSessionRepository.delete(jwtSession);
        if (!jwtSession.isExpired()) {
            jwtBlacklist.addBlacklist(jwtSession.getAccessToken(), jwtSession.getExpirationTime());
        }
    }

    // 토큰에서 주요 정보(JwtObject) 추출
    // 추출 실패 = 지정한 토큰과 형태가 다름 -> 유효하지 않은 토큰
    public JwtObject parse(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            Map<String, Object> jsonObject = payload.toJSONObject();
            return new JwtObject(
                objectMapper.convertValue(jsonObject.get("iat"), Instant.class),
                objectMapper.convertValue(jsonObject.get("exp"), Instant.class),
                objectMapper.convertValue(jsonObject.get("userDto"), UserDto.class),
                token
            );
        } catch (ParseException e) {
            log.error(e.getMessage());
            throw new DiscodeitException(ErrorCode.INVALID_JWT_TOKEN, Map.of("token", token));
        }
    }

    // 현재 로그인 중인 유저 확인용
    public List<JwtSession> getActiveJwtSessions() {
        return jwtSessionRepository.findAllByExpirationTimeAfter(Instant.now());
    }

    // ----------------------------------

    // 액세스 토큰 발급
    private JwtObject generateAccessToken(UserDto userDto) {
        return generateToken(userDto,
            jwtProperties.getAccessToken().getValiditySeconds(),
            TokenType.ACCESS);
    }

    // 리프레시 토큰 발급
    private JwtObject generateRefreshToken(UserDto userDto) {
        return generateToken(userDto,
            jwtProperties.getRefreshToken().getValiditySeconds(),
            TokenType.REFRESH);
    }

    // 토큰 생성
    // Nimbus JOSE + JWT 라이브러리 이용하여 생성
    private JwtObject generateToken(UserDto userDto, long validitySeconds,
        TokenType tokenType) {
        Instant issueTime = Instant.now();
        Instant expirationTime = issueTime.plusSeconds(validitySeconds);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(userDto.getUsername())
            .claim("userDto", userDto)
            .issueTime(new Date(issueTime.toEpochMilli()))
            .expirationTime(new Date(expirationTime.toEpochMilli()))
            .claim("type", tokenType.name())
            .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            signedJWT.sign(new MACSigner(jwtProperties.getSecret()));
        } catch (JOSEException e) {
            log.error(e.getMessage());
            throw new DiscodeitException(ErrorCode.INVALID_TOKEN_SECRET, Map.of());
        }
        String token = signedJWT.serialize();
        return new JwtObject(issueTime, expirationTime, userDto, token);
    }


}
