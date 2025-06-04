package com.sprint.mission.discodeit.security.jwt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jwt")
@Component
@Data
@Validated
public class JwtProperties {

    @NotBlank
    private String issuer = "server"; // iss=발급자

    @NotBlank
    @Size(min = 32, message = "JWT secret must be at least 32 characters")
    private String secret; // 토큰 서명 비밀 키, 필수값, 32자 이상

    @Valid
    private TokenConfig accessToken = new TokenConfig();

    @Valid
    private TokenConfig refreshToken = new TokenConfig();

    // 내부 클래스로 Access/Refresh Token에 공통 적용될 구성
    @Data
    public static class TokenConfig {

        @Min(value = 60, message = "Token validity must be at least 60 seconds")
        private long validitySeconds = 900; // 15분
    }
}