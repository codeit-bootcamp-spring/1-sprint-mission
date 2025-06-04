package com.sprint.mission.discodeit.security;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {
  Optional<JwtSession> findByRefreshToken(String refreshToken);

  void deleteAllByUserId(UUID userId);
}
