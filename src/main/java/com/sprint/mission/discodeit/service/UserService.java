package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UsersDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User create(UsersDTO usersDTO, byte[] profileImage);
    User update(String id, UsersDTO usersDTO, byte[] profileImage);
    void delete(String id);
    UserDTO find(String id);
    List<UsersDTO> findAll();
    void updateOnlineStatus(String userId, boolean online);
}