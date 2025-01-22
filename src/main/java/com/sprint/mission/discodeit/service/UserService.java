package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
   void createUser(User user);
   Optional<User> findUser(UUID id);
   List<User> findAllUsers();
   void updateUserName(UUID id,String userName);
   void updateUserEmail(UUID id,String userEmail);
   void deleteUser(UUID id);
}
