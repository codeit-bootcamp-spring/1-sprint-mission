package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JwtSession extends BaseUpdatableEntity {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 500)
    private String accessToken;

    @Column(nullable = false, length = 500)
    private String refreshToken;

    @Column(nullable = false)
    private Instant accessTokenExpiresAt;

    @Column(nullable = false)
    private Instant refreshTokenExpiresAt;

    @Column(nullable = false)
    private int refreshCount;

    @Column(columnDefinition = "boolean default false")
    private boolean revoked;

    public boolean isRefreshTokenValid() {
        return !revoked && refreshTokenExpiresAt.isAfter(Instant.now());
    }

    public boolean isAccessTokenValid() {
        return accessTokenExpiresAt.isAfter(Instant.now());
    }

    public void revoke() {
        this.revoked = true;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateAccessTokenExpiresAt(Instant expiresAt) {
        this.accessTokenExpiresAt = expiresAt;
    }

    public void updateRefreshTokenExpiresAt(Instant expiresAt) {
        this.refreshTokenExpiresAt = expiresAt;
    }

    public void incrementRefreshCount() {
        this.refreshCount++;
    }
}
