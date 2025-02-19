package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository { //crud
   void save(User user);
   Optional<User> findById(UUID userId);
   Map<UUID, User> findAll();
   void delete(UUID userId);
   boolean existsById(UUID userId); //해당 유저가 있는지 없는지 확인
   /*Optional<User> findByUsernameAndPassword(String userName, String password);*/

}
