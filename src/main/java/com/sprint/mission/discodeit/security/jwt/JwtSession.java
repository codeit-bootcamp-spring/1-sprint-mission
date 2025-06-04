package com.sprint.mission.discodeit.security.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, length = 512)
  private String accessToken;

  @Setter
  @Column(nullable = false, length = 512)
  private String refreshToken;

  @Setter
  private Instant issuedAt;

  @Setter
  private Instant expiresAt;

  @Builder
  private JwtSession(String username, String accessToken, String refreshToken,
      Instant issuedAt,
      Instant expiresAt) {
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.issuedAt = issuedAt;
    this.expiresAt = expiresAt;
  }

}
