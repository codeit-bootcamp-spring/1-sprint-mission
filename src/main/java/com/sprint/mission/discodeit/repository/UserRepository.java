package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.entity.status.AccountStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  List<User> findByAccountStatus(AccountStatus accountStatus);
}
