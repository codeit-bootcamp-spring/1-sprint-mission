package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Gender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  boolean save(UserDto user);

  Optional<UserDto> findById(UUID id);

  List<UserDto> findAll();

  boolean updateOne(UUID id, String name, int age, Gender gender);

  boolean deleteOne(UUID id);
}
