package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

  Optional<JwtSession> findJwtSessionByRefreshToken(String refreshToken);

  void deleteByUser(User user);

  List<JwtSession> findByUser(User user);

  User user(User user);
}
