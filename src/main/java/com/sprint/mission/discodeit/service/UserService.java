package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto create(UserCreateRequest userRequest, MultipartFile file);
    UserDto find(UUID userId);
    List<UserDto> findAll();
    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile file);
    void delete(UUID userId);
    void validateDuplicateName(String name);
    void validateDuplicateEmail(String email);
}
