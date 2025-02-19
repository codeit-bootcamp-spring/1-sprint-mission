package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserDTO.createDTO userCreateDTO);
    UserDTO.responseDTO find(UUID userId);
    List<UserDTO.responseDTO> findAll();
    User update(UserDTO.updateDTO userUpdateDTO);
    void delete(UUID userId);
}
