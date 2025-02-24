package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.web.dto.CheckUserDto;
import com.sprint.mission.discodeit.web.dto.UserUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
   User createUser(User user);
   Optional<CheckUserDto> findUser(UUID id);
   Optional<CheckUserDto> findByloginId(String loginId);
   void updateProfile(UUID id, BinaryContent newProfile);
   List<CheckUserDto> findAllUsers();
   void updateUser(UUID id, UserUpdateDto userParam);
   void deleteUser(UUID id);

   void updateuserStatus(UUID userId);
}
