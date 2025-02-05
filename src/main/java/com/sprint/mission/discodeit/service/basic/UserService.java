package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userService.UserDTO;
import com.sprint.mission.discodeit.dto.userService.UserProfileImageRequest;
import com.sprint.mission.discodeit.dto.userService.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest, UserProfileImageRequest profileImage);
    UserDTO find(UUID userId);
    List<UserDTO> findAll();
    UserDTO update(UserUpdateRequest userUpdateRequest, UserProfileImageRequest profileImage);
    void delete(UUID userId);
}
