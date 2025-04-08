package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserCreateResponse join(UserCreateRequest request, MultipartFile file) throws IOException;

  UserCreateResponse findById(UUID userId);

  List<UserCreateResponse> findAll();

  UUID delete(UUID userId);

  UserUpdateResponse update(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profile) throws IOException;

  UserStatusUpdateResponse updateOnlineStatus(UUID userId, UserStatusUpdateRequest request);

}
