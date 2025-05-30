package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JwtSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  private UUID userId;
  private String accessToken;
  private String refreshToken;

  private LocalDateTime issuedAt;
  private LocalDateTime expiresAt;

  public JwtSession() {
  }

  public JwtSession(UUID userId, String accessToken, String refreshToken, LocalDateTime issuedAt,
      LocalDateTime expiresAt) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.issuedAt = issuedAt;
    this.expiresAt = expiresAt;
  }
}
