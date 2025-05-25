package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;

public interface AuthService {

    // 사용자 권한 변경하기
    UserResponse changeUserRole(UserRoleUpdateRequest request);
}
