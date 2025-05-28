package com.sprint.mission.discodeit.security.jwt;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JwtSession {

  @Id
  @GeneratedValue
  private Long id;

  private UUID userId;

  private String accessToken;

  private String refreshToken;

  private Instant issuedAt;

  private Instant expiresAt;

  private String username;
  private String role;

}