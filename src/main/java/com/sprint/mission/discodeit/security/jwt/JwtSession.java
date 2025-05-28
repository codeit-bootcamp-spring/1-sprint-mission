package com.sprint.mission.discodeit.security.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@Table(name = "jwt_sessions")
@AllArgsConstructor
@NoArgsConstructor
public class JwtSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "access_token", nullable = false)
  private String accessToken;

  @Column(name = "refresh_token", nullable = false)
  private String refreshToken;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime refreshTokenExpiresAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime refreshTokenCreatedAt;

  @Column(name = "revoked", nullable = false)
  private boolean revoked = false; // 로그아웃 시 true

  @Column(name = "replaced_by")
  private String replacedBy;  // 회전 시 새 토큰 ID

  public void updatedRevoked(boolean isRevoked) {
    this.revoked = isRevoked;
  }

  public void updatedReplacedBy(String replacedBy) {
    this.replacedBy = replacedBy;
  }

  // 토큰이 유효한지
  public boolean isValid() {
    return !revoked && refreshTokenExpiresAt.isAfter(LocalDateTime.now());
  }

  // 토큰이 만료되었는지
  public boolean isExpired() {
    return refreshTokenExpiresAt.isBefore(LocalDateTime.now());
  }
}
