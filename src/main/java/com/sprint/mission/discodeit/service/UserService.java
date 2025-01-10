package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
   void createUser(User user);
   Optional<User> userList(UUID id);
   List<User> allUserList();
   void updateUser(UUID id,User user);
   void deleteUser(UUID id);
}
