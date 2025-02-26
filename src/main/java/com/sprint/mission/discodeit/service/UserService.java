package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserService {


    UUID create(UserCreateDTO userCreateDTO);
    UserFindDTO find(UUID id);
    List<UserFindDTO> findAll();
    User update(UUID userId, UserUpdateDTO dto);
    UUID delete(UUID id);
}
