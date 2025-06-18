package com.sprint.mission.discodeit.security.jwt;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

    Optional<JwtSession> findByRefreshToken(String refreshToken);

    void deleteAllByUserId(UUID userId);

    boolean existsByAccessToken(String accessToken);

    Optional<JwtSession> findFirstByUserId(UUID userId);
}
