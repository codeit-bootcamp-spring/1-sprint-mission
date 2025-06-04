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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 1000)
    private String accessToken;
    
    @Column(nullable = false, length = 1000)
    private String refreshToken;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime accessTokenExpiresAt;
    
    @Column(nullable = false)
    private LocalDateTime refreshTokenExpiresAt;
    
    @Column(nullable = false)
    private boolean isValid;
    
    @Builder.Default
    private LocalDateTime invalidatedAt = null;
    
    public void invalidate() {
        this.isValid = false;
        this.invalidatedAt = LocalDateTime.now();
    }
    
    public boolean isAccessTokenExpired() {
        return LocalDateTime.now().isAfter(accessTokenExpiresAt);
    }
    
    public boolean isRefreshTokenExpired() {
        return LocalDateTime.now().isAfter(refreshTokenExpiresAt);
    }
    
    public void updateTokens(String accessToken, String refreshToken, 
                            LocalDateTime accessTokenExpiresAt, 
                            LocalDateTime refreshTokenExpiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        this.isValid = true;
        this.invalidatedAt = null;
    }
}