package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtSession extends BaseUpdatableEntity {

  private UUID userId;

  @Column(nullable = false, length = 500)
  private String accessToken;

  @Column(nullable = false, length = 500)
  private String refreshToken;

  public JwtSession(UUID userId, String accessToken, String refreshToken) {
    this.userId = userId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public void update(String newAccessToken, String newRefreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
