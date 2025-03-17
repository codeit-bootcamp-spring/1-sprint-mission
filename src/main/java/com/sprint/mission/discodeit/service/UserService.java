package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto createUser(CreateUserRequest createUserRequest, MultipartFile profileImageFile);

  UserDto findUserByIdOrThrow(UUID id);

  List<UserDto> findAllUsers();

  UserDto updateUser(UUID userId, UpdateUserRequest updateUserRequest,
      MultipartFile profileImageFile);

  void deleteUser(UUID id);
}
