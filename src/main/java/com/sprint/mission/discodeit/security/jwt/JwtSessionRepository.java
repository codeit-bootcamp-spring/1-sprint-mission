package com.sprint.mission.discodeit.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {

  Optional<JwtSession> findByRefreshTokenAndIsValidTrue(String refreshToken);

  Optional<JwtSession> findByAccessTokenAndIsValidTrue(String accessToken);

  @Modifying
  @Query("UPDATE JwtSession j SET j.isValid = false, j.invalidatedAt = CURRENT_TIMESTAMP "
      + "WHERE j.user.id = :userId AND j.isValid = true")
  void invalidateAllUserSessions(UUID userId);

}