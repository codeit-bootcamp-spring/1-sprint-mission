package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserService {

  User createUser(UserCreateDTO userCreateDTO);

  UserFindDTO findUserDTO(UUID id);

  List<UserFindDTO> findAllUserDTO();

  User updateUser(UUID id, UserUpdateDTO userUpdateDTO);

  void deleteUser(UUID id);

  //user온라인상태업데이트
  UserStatusUpdateDTO updateUserStatus(UUID id, UserStatusUpdateDTO userUserStatusUpdateDTO);


  //이름, 이메일 존재 검증
  Boolean isNameExist(String name);

  Boolean isEmailExist(String email);

}
