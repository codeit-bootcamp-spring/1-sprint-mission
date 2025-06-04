package com.spirnt.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

    Optional<JwtSession> findByRefreshToken(String refreshToken);

    boolean existsByUserIdAndExpirationTimeAfter(UUID userId, Instant after);

    Optional<JwtSession> findByUserId(UUID userId);

    // 현재 로그인 중인 유저 확인용
    List<JwtSession> findAllByExpirationTimeAfter(Instant after);
}
