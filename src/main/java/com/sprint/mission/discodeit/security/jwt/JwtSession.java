package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jwt_sessions")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JwtSession {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "refresh_token", length = 1000)
  private String refreshToken;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "is_revoked", nullable = false)
  private boolean revoked;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.revoked = false;
  }
}
