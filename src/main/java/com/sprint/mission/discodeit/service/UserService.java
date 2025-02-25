package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserRequest request, MultipartFile userProfileImage);
    List<UserResponse> findAll();
    UserResponse findById(UUID id);
    User findByIdOrThrow(UUID id);
    UserResponse update(UUID id, UserRequest request, MultipartFile userProfileImage);
    void deleteById(UUID id);
}
