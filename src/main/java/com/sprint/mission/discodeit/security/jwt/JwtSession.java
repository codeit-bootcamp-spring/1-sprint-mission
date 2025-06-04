package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@NoArgsConstructor
public class JwtSession {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false)
  private UUID id;

  @OneToOne(optional = false)
  private User user;

  @Column(nullable = false, unique = true)
  private String accessToken;

  @Column(nullable = false, unique = true)
  private String refreshToken;


  public JwtSession(User user, String accessToken, String refreshToken) {
    this.user = user;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public void updateTokens(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
