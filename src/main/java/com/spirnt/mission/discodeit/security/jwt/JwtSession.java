package com.spirnt.mission.discodeit.security.jwt;

import com.spirnt.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_sessions")
@NoArgsConstructor
@Getter
public class JwtSession extends BaseEntity {

    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID userId;
    @Column(columnDefinition = "varchar(512)", nullable = false, unique = true)
    private String accessToken;
    @Column(columnDefinition = "varchar(512)", nullable = false, unique = true)
    private String refreshToken;
    @Column(columnDefinition = "timestamp with time zone", nullable = false)
    private Instant expirationTime;

    public JwtSession(UUID userId, String accessToken, String refreshToken,
        Instant expirationTime) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }

    public boolean isExpired() {
        return this.expirationTime.isBefore(Instant.now());
    }

    public void update(String accessToken, String refreshToken, Instant expirationTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }
}
