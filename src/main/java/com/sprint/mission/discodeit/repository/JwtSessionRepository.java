package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.security.jwt.JwtSession;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  Optional<JwtSession> findByUser_Id(UUID userId);
}
