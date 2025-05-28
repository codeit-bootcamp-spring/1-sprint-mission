package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import java.util.UUID;

public interface AuthService {

  UserResponse getUserById(UUID userId);

  UserResponse updateRole(RoleUpdateRequest request);

  void initAdmin();
}
