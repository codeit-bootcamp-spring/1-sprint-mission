package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateDTO;
import com.sprint.mission.discodeit.dto.UserReadDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void create(UserCreateDTO userDTO);
    Optional<UserReadDTO> read(UUID id);
    List<UserReadDTO> readAll();
    void update(UUID id, UserUpdateDTO userDTO);
    void delete(UUID id);
}
