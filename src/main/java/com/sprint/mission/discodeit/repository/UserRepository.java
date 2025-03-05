package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {  // ✅ JpaRepository를 상속하면 existsById() 사용 가능
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
