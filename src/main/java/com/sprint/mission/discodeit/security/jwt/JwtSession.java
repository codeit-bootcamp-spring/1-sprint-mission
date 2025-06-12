package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession extends BaseUpdatableEntity {

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false, unique = true)
  private String accessToken;

  @Column(nullable = false, unique = true)
  private String refreshToken;

  @Column(nullable = false)
  private Instant expirationTime;

  @Builder(access = AccessLevel.PRIVATE)
  private JwtSession(UUID userId, String accessToken, String refreshToken, Instant expirationTime) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationTime= expirationTime;
  }

  public static JwtSession create(UUID userId, String accessToken, String refreshToken, Instant expirationTime) {
    return JwtSession.builder()
        .userId(userId)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .expirationTime(expirationTime)
        .build();
  }

  public void updateToken(String accessToken, String refreshToken, Instant expirationTime) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationTime = expirationTime;
  }

  public boolean isExpired() {
    return this.expirationTime.isBefore(Instant.now());
  }

}
