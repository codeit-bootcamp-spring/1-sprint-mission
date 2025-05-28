package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.security.jwt.JwtSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  void deleteAllByRefreshToken(String refreshToken);

  boolean existsByRefreshToken(String refreshToken);

  boolean existsByUser_Id(UUID userId);

  void deleteAllByUser_Id(UUID userId);

  List<JwtSession> findAllByUser_Id(UUID userId);

  List<JwtSession> findByAccessToken(String accessToken);

  boolean existsByAccessToken(String accessToken);

  Optional<JwtSession> findFirstByUser_Id(UUID userId);
}
