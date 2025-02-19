package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.user.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserDTO create(CreateUserRequest request);

    UserDTO find(UUID userId);

    List<UserDTO> getOnlineUsers();

    List<UserDTO> findAll();

    User update(UpdateUserRequest request);

    UserDTO delete(UUID id);
}
