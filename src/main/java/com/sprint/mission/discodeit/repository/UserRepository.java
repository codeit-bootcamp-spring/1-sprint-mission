package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  // 검증을 위한 쿼리 메서드 추가 -> 모든 User 객체를 불러와 비교하는 방식은 비효율적이기 때문에
  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

}