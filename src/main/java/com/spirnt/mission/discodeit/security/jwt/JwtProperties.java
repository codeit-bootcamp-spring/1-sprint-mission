package com.spirnt.mission.discodeit.security.jwt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Getter
@Setter
@Validated
public class JwtProperties {

    @NotBlank
    private String issuer;
    @NotBlank
    @Size(min = 32, message = "JWT secret must be at least 32 characters")
    private String secret;
    @Valid
    private TokenConfig accessToken = new TokenConfig();
    @Valid
    private TokenConfig refreshToken = new TokenConfig();

    // accessToken, refreshToken에 '만료 시간' 속성 공통 적용
    @Getter
    @Setter
    public static class TokenConfig {

        @Min(value = 60, message = "Token validity must be at least 60 seconds")
        private long validitySeconds = 60 * 15;
    }
}
