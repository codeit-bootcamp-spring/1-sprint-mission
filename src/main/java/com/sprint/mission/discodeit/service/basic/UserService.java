package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.dto.userService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userService.UserDTO;
import com.sprint.mission.discodeit.dto.userService.UserProfileImageRequest;
import com.sprint.mission.discodeit.dto.userService.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequestDTO> profileCreateRequest);    UserDTO find(UUID userId);
    List<UserDTO> findAll();
    User update(UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequestDTO> optionalProfileCreateRequest);
    void delete(UUID userId);
}
