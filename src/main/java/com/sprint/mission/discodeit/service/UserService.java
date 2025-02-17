package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.User;
import com.sprint.mission.discodeit.dto.form.CheckUserDto;
import com.sprint.mission.discodeit.dto.form.UserUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
   void createUser(User user);
   Optional<CheckUserDto> findUser(UUID id);
   Optional<CheckUserDto> findByloginId(String loginId);
   List<User> findAllUsers();
   void updateUser(UUID id, UserUpdateDto userParam);
   void deleteUser(UUID id);
}
