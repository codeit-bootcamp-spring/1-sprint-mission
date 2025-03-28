package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto create(UserDto userDTO, byte[] profileImage);
    UserDto createWithProfileImage(UserDto userDTO, MultipartFile profileImage) throws IOException;
    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, byte[] profileImage);
    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile profileImage) throws IOException;
    void delete(UUID userId);
    UserDto find(UUID userId);
    List<UserDto> findAll();
    void updateOnlineStatus(UUID userId, boolean online);
    UserStatusDto updateUserStatus(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
    UserDto findByEmail(String email);
}