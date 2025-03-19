package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto create(UserCreateDTO dto,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  UserDto find(UUID id);

  List<UserDto> findAll();

  UserDto update(UUID id, UserUpdateDTO dto,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  void delete(UUID id);
}
