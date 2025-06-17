package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  Optional<JwtSession> findByUserId(UUID userId);

  List<JwtSession> findAllByExpirationTimeAfter(Instant after);
}
