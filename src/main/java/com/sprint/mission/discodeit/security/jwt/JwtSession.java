package com.sprint.mission.discodeit.security.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@NoArgsConstructor
public class JwtSession {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JoinColumn(name = "username")
  @Column(nullable = false)
  private UserDetails userDetails;
  private String AccessToken;
  private String RefreshToken;

  public JwtSession(UserDetails userDetails, String accessToken, String refreshToken) {
    this.userDetails = userDetails;
    this.AccessToken = accessToken;
    this.RefreshToken = refreshToken;
  }

  public void setNewTokens(String accessToken, String refreshToken) {
    this.AccessToken = accessToken;
    this.RefreshToken = refreshToken;
  }
}
