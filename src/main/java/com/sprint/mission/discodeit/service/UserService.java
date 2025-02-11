package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UserResponseDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(CreateUserDTO createUserDTO);
    UserResponseDTO find(UUID userId);
    List<UserResponseDTO> findAll();
    User update(UUID userId, UpdateUserDTO updateUserDTO);
    void delete(UUID userId);
}
