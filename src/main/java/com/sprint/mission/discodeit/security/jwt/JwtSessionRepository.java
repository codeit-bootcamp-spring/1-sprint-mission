package com.sprint.mission.discodeit.security.jwt;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JwtSessionRepository extends JpaRepository<JwtSession, String> {
    List<JwtSession> findAllByUserId(UUID userId);

    boolean existsByUserIdAndRevokedFalse(UUID userId);

    @Transactional
    @Modifying
    @Query("UPDATE JwtSession js SET js.revoked = true WHERE js.user.id = :userId AND js.revoked = false")
    void updateRevokeAllByUserId(@Param("userId") UUID userId);

    Optional<JwtSession> findByAccessToken(String accessToken);
}