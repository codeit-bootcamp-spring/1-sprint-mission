package com.sprint.mission.discodeit.security.jwt;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {

  @EntityGraph(attributePaths = "user")
  Optional<JwtSession> findByRefreshTokenAndRevokedFalse(String refreshToken);

  void deleteAllByUserId(UUID userId);

  @EntityGraph(attributePaths = "user")
  Optional<JwtSession> findByRefreshToken(String refreshToken);
}
