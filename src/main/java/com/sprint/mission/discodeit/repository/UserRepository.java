package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository { //crud
   User save(User user);
   Optional<User> findById(UUID userId);
   Optional<User> findByUsername(String username);
   List<User> findAll();
   void deleteById(UUID userId);
   boolean existsById(UUID userId); //해당 유저가 있는지 없는지 확인
   boolean existsByEmail(String userEmail);
   boolean existsByUsername(String username);
}
