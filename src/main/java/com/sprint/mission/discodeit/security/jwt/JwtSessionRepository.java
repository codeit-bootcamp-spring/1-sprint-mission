package com.sprint.mission.discodeit.security.jwt;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

    Optional<JwtSession> findByUserId(UUID userId);

    Optional<JwtSession> findByRefreshToken(String refreshToken);

    void deleteByUserId(UUID userId);

    @Modifying
    @Query("UPDATE JwtSession js "
        + "SET js.revoked = true "
        + "WHERE js.userId = :userId AND js.revoked = false")
    void revokeAllSessionsByUserId(@Param("userId") UUID userId);

}
