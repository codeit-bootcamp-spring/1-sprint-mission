package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

  //생성
  UserDto create(CreateUserDto createUserDto);

  //생성 with profile image
  UserDto create(CreateUserDto createUserDto, MultipartFile multipartFile);

  //모두 읽기
  List<UserDto> findAll();

  //읽기
  //단건 조회 - UUID
  UserDto findById(String userId);

  //단건 조회 - 이메일로 조회
  UserDto findByEmail(String email);

  //다건 조회 - 사용자 상태
  //List<User> getUserByUserStatus(UserStatus userStatus);

  //수정
  UserDto updateUser(String userId, UpdateUserDto updateUserDto);

  //수정 - 프로필 이미지와 함께
  UserDto updateUser(String userId, UpdateUserDto updateUserDto, MultipartFile multipartFile);

  //삭제
  boolean deleteUser(String userId);

  UserDto findByUsername(String username);

  UserDto updateUserRole(RoleUpdateRequest roleUpdateRequest);
}
