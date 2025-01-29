package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserRepository { //crud
   void save(User user);
   User findById(UUID userId);
   void delete(UUID userId);
   Map<UUID, User> findAll();
}
