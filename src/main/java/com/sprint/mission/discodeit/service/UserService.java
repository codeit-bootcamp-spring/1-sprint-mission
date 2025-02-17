package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService extends CRUDService<UserRequest, UserResponse>{
    UserResponse create(UserRequest request, MultipartFile file);
//    List<User> searchByUser();
//    User searchByUserId(UUID userId);
}
