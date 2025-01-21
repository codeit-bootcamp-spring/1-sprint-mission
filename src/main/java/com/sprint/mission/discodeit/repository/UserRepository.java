package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

  User createUser(User user);
  Optional<User> getUserById(String id);
  List<User> getAllUsers();
  User updateUser(User user);
  void deleteUser(String userId);
}
