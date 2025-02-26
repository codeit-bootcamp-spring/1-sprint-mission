package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserMasterFacade {
  CreateUserResponse createUser(CreateUserRequest request, MultipartFile profile);
  CreateUserResponse updateUser(String userId, MultipartFile profile, UserUpdateDto updateDto);
  UserResponseDto findUserById(String id);
  List<UserResponseDto> findAllUsers();
  void deleteUser(String id);
}
