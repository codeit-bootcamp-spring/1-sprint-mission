package com.sprint.mission.discodeit.security.jwt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

// application.yml 에서 "jwt" 로 시작하는 설정을 자동 바인딩
@ConfigurationProperties(prefix = "jwt")
@Component
@Data
@Validated
public class JwtProperties {

  @NotBlank
  private String issuer = "demo-api"; // iss=발급자

  @NotBlank
  @Size(min = 32, message = "JWT secret must be at least 32 characters")
  private String secret; // 토큰 서명 비밀 키, 필수값, 32자 이상

  @Valid
  private AccessTokenConfig accessToken = new AccessTokenConfig();

  @Valid
  private RefreshTokenConfig refreshToken = new RefreshTokenConfig();

  // Access Token 전용 설정 (15분 기본값)
  @Data
  public static class AccessTokenConfig {

    @Min(value = 60, message = "Token validity must be at least 60 seconds")
    private long validitySeconds = 900; // 15분
  }

  // Refresh Token 전용 설정 (30일 기본값)
  @Data
  public static class RefreshTokenConfig {

    @Min(value = 60, message = "Token validity must be at least 60 seconds")
    private long validitySeconds = 2592000; // 30일
  }
}
