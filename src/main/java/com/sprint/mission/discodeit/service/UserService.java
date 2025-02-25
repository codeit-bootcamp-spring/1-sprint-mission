package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(UserCreate userCreateDTO, Optional<BinaryContentCreateDto> profileCreateDto);
    UserDto findById(UUID userId);
    List<UserDto> findAll();
    User update(UUID userId, UserUpdate userUpdate, Optional<BinaryContentCreateDto> profileCreateDto);
    void delete (UUID userId);
}
