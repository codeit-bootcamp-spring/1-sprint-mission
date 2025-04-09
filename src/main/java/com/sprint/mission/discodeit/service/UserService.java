package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto create(UserCreateRequest request,
      Optional<MultipartFile> nullableFile);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<MultipartFile> nullableFile);

  void delete(UUID userId);

}

