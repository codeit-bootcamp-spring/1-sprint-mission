package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface UserFacade {

  UserResponseDto createUser(CreateUserRequest request, MultipartFile profile);

  UserResponseDto updateUser(String userId, MultipartFile profile, UserUpdateDto updateDto,
      UserDetails userDetails);

  UserResponseDto findUserById(String id);

  List<UserResponseDto> findAllUsers();

  void deleteUser(String id);
}
