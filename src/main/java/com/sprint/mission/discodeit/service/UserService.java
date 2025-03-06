package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UsersDto create(UsersDto usersDTO, byte[] profileImage);
    UsersDto update(UUID id, UsersDto usersDTO, byte[] profileImage);
    void delete(UUID id);
    UserDto find(UUID id);
    List<UsersDto> findAll();
    void updateOnlineStatus(UUID userId, boolean online);
    UserDto findByEmail(String email);
}