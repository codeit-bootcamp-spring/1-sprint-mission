package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  User create(UserCreateDTO dto,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  UserFindDTO find(UUID id);

  List<UserFindDTO> findAll();

  User update(UUID id, UserUpdateDTO dto,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  UUID delete(UUID id);
}
