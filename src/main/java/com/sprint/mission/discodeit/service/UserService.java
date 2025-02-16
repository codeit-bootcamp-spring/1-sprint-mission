package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
  boolean createUser(User user);

  Optional<User> readUser(UUID id);

  List<User> readAllUsers();

  void updateUser(UUID id, String name, int age, Gender gender);

  void deleteUser(UUID id);

  List<User> getAllUsers();
}

