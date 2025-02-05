package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public interface UserService {
  void createUser(String password, String name);
  
  void updateUserName(UUID id, String newName);
  
  void updateUserPassword(UUID id, String newPassword);
  
  void removeUser(UUID id);
  
  User findUserByName(String name);
}
