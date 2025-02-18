package com.sprint.mission.discodeit.service.interfacepac;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.response.user.UserDTO;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateProfileImageDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO create(UserCreateDTO userCreateDTO, BinaryContentCreateRequest binaryContentCreateRequest);
    UserDTO find(UUID userId);
    List<UserDTO> findAll();
    UserDTO update(UserUpdateDTO userUpdateDTO, UserUpdateProfileImageDTO userUpdateProfileImageDTO);
    void delete(UUID userId);
}
