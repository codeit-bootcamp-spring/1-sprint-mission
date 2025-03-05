package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    UsersDto create(UsersDto usersDTO, byte[] profileImage);
    UsersDto update(String id, UsersDto usersDTO, byte[] profileImage);
    void delete(String id);
    UserDto find(String id);
    List<UsersDto> findAll();
    void updateOnlineStatus(String userId, boolean online);
}