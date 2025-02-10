package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import java.util.List;
import java.util.UUID;

public interface UserService {
  void createUser(CreateUserDto createUserDto);
  
  FindUserDto findById(UUID id);
  
  FindUserDto findByName(String name);
  
  List<FindUserDto> findAll();
  
  void updateName(UpdateUserDto updateUserDto);
  
  void updatePassword(UpdateUserDto updateUserDto);
  
  void updateProfileImage(UpdateUserDto updateUserDto);
  
  void remove(UUID id);
  
}
