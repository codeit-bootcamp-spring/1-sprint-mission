package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.JwtSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findByAccessToken(String token);

  Optional<JwtSession> findByRefreshToken(String refreshToken);

  List<JwtSession> findAllByUserId(UUID id);
}
