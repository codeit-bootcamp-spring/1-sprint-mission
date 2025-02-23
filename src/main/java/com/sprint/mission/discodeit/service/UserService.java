package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest,
                Optional<BinaryContentCreateRequest> profileCreateRequest);
    UserDTO find(UUID userId);
    List<UserDTO> findAll();
    User update(UUID userId , UserUpdateRequest userUpdateRequest,
                Optional<BinaryContentCreateRequest> profileCreateRequest);
    void delete(UUID userId);
}
