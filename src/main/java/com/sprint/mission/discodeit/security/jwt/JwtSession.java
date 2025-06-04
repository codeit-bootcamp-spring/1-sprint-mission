package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "jwt_sessions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtSession {

    @Id
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, length = 1000)
    private String accessToken;

    private Instant issuedAt;
    private Instant expiresAt;

    private boolean revoked;

    public void revoke() {
        this.revoked = true;
    }
}