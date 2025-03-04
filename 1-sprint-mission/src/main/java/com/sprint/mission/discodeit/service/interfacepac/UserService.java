package com.sprint.mission.discodeit.service.interfacepac;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

  UserResponseDTO create(UserCreateDTO userCreateDTO,
      BinaryContentCreateRequest binaryContentCreateRequest);

  UserResponseDTO find(UUID userId);

  List<UserResponseDTO> findAll();

  UserResponseDTO update(UserUpdateDTO userUpdateDTO,
      BinaryContentCreateRequest binaryContentCreateRequest);

  void delete(UUID userId);
}
