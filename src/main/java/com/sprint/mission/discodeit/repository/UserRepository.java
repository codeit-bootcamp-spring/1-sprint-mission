package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  // 아이디로 유저 검색
  Optional<User> findByUsername(String username);

  // 이메일 존재하는지 확인
  boolean existsByEmail(String email);

  // 아이디 존재하는지 확인
  boolean existsByUsername(String username);

  // 모든 유저 검색
  @Query("SELECT u FROM User u "
      + "LEFT JOIN FETCH u.profile "
      + "JOIN FETCH u.status")
  List<User> findAllWithProfileAndStatus();
}