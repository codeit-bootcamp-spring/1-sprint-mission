package com.sprint.mission.discodeit.security.jwt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Validated
@RequiredArgsConstructor
public class JwtProperties {

    @NotBlank
    private final String issuer; // iss=발급자

    @NotBlank
    @Size(min = 32, message = "JWT secret must be at least 32 characters")
    private final String secret; // 토큰 서명 비밀 키, 필수값, 32자 이상

    @Valid
    private final TokenConfig accessToken;

    @Valid
    private final TokenConfig refreshToken;

    @Getter
    @Validated
    @RequiredArgsConstructor
    public static class TokenConfig {

        @Min(value = 60, message = "Token validity must be at least 60 seconds")
        private final long validitySeconds;
    }
}
