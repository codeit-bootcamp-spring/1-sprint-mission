package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class JwtSession {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Setter
  @Column(length = 2000, nullable = false)
  private String accessToken;

  @Column(length = 2000, nullable = false)
  private String refreshToken;

  public JwtSession(String accessToken, String refreshToken, User user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.user = user;
  }

  public JwtSession(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
