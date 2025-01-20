package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  User createUser(User user);

  Optional<User> readUserById(String id);

  List<User> readAllUsers();

  void updateUser(String id, UserUpdateDto updatedUser, String originalPassword);

  void deleteUser(String id, String password);

}
