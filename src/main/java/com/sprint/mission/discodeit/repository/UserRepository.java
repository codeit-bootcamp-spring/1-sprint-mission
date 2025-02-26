package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, String>{

  User create(User user);
  Optional<User> findById(String id);
  Optional<User> findByUsername(String username);
  List<User> findAll();
  User update(User user);
  void delete(String userId);
  void clear();
}
