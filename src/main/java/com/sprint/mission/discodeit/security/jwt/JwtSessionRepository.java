package com.sprint.mission.discodeit.security.jwt;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  void deleteByRefreshToken(String refreshToken);

  void deleteByUserId(UUID userId);
}