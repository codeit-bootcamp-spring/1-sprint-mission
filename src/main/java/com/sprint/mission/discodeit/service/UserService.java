package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthDto;
import com.sprint.mission.discodeit.dto.UserDto;
import java.util.List;
import java.util.UUID;

public interface UserService {
  UserDto createUser(UserDto paramUserDto);
  UserDto findUserById(UUID paramUUID);
  List<UserDto> findAllUsers();
  UserDto updateUser(UUID paramUUID, UserDto paramUserDto);
  void deleteUser(UUID paramUUID);
  AuthDto.LoginResponse login(AuthDto.LoginRequest paramLoginRequest);
}
