package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  boolean save(User user);

  Optional<User> findById(UUID id);

  List<User> findAll();

  boolean updateOne(UUID id, String name, int age, Gender gender);

  boolean deleteOne(UUID id);
}
