package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  User create(UserCreateRequest userCreateRequest, MultipartFile profile);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  User update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile profile);

  void delete(UUID userId);
}
