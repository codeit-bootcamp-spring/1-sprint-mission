package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserServiceCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceFindDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {


    UUID create(UserServiceCreateDTO userServiceCreateDTO);
    UserServiceFindDTO find(UUID id);
    List<UserServiceFindDTO> findAll();
    User update(UserServiceUpdateDTO userServiceUpdateDTO);
    UUID delete(UUID id);
}
