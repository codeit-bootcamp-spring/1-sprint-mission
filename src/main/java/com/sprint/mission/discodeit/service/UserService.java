package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User create(UserDTO userDTO, byte[] profileImage);
    User update(String id, UserDTO userDTO, byte[] profileImage);
    void delete(String id);
    UserDTO find(String id);
    List<UserDTO> findAll();
}