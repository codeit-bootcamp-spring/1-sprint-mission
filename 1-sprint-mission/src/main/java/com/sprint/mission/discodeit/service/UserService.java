package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserCreateDTO;
import com.sprint.mission.discodeit.dto.request.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.response.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserProfileImageDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO create(UserCreateDTO userCreateDTO, UserProfileImageDTO userProfileImageDTO);
    UserDTO find(UUID userId);
    List<UserDTO> findAll();
    UserDTO update(UserUpdateDTO userUpdateDTO, UserProfileImageDTO userProfileImageDTO);
    void delete(UUID userId);
}
