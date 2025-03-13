package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UserService extends CRUDService<UserRequest, UserResponse>{
    UserResponse create(UserRequest request, MultipartFile file) throws IOException;
}
