package com.sprint.mission.discodeit.security.jwt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@Validated
public class JwtProperties {

    @NotBlank
    private String issuer = "discodeit-api"; // iss=발급자

    @NotBlank
    @Size(min = 32, message = "JWT secret must be at least 32 characters")
    private String secret; // 토큰 서명 비밀 키, 필수값, 32자 이상

    @Valid
    private TokenConfig accessToken = new TokenConfig();

    @Valid
    private TokenConfig refreshToken = new TokenConfig();

    // Token 에 공통 적용될 구성
    @Data
    public static class TokenConfig {

        @Min(value = 60, message = "Token validity must be at least 60 seconds")
        private long validitySeconds = 900; // 15분
    }
}
