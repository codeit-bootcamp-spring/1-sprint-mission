package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

  User create(User user);
  Optional<User> findById(String id);
  List<User> findAll();
  User update(User user);
  void delete(String userId);

  void clear();
}
