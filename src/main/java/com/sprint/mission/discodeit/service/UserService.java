package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto createUser(CreateUserRequest createUserRequest, MultipartFile profileImageFile);

  FindUserResponse findUserByIdOrThrow(UUID id);

  List<FindUserResponse> findAllUsers();

  UserDto updateUser(UUID userId, UpdateUserRequest updateUserRequest,
      MultipartFile profileImageFile);

  void deleteUser(UUID id);
}
