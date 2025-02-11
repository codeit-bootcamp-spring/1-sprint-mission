package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  User save(User paramUser);
  Optional<User> findById(UUID paramUUID);
  List<User> findAll();
  Optional<User> findByUsername(String paramString);
  boolean existsByUsername(String paramString);
  void deleteById(UUID paramUUID);
}
