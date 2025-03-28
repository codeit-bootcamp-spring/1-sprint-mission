package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto create(UserDto userDTO, byte[] profileImage);
    UserDto createWithProfileImage(UserDto userDTO, MultipartFile profileImage) throws IOException;
    UsersDto update(UUID id, UsersDto usersDTO, byte[] profileImage);
    UsersDto update(UUID id, UsersDto usersDTO, MultipartFile profileImage) throws IOException;
    void delete(UUID id);
    UserDto find(UUID id);
    List<UsersDto> findAll();
    void updateOnlineStatus(UUID userId, boolean online);
    UserDto findByEmail(String email);
}