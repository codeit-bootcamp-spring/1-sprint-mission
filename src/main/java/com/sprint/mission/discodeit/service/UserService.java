package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserResponse createUser(UserRequest.Create request, MultipartFile userProfileImage);

    List<UserResponse> findAll();

    UserResponse findById(UUID id);

    UserResponse findByUsername(String username);

    UserResponse update(UUID id, UserRequest.Update request, MultipartFile userProfileImage);

    void deleteById(UUID id);
}
