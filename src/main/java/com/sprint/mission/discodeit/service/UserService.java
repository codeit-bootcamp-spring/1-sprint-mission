package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.user.UserDeleteResponse;
import com.sprint.mission.discodeit.service.dto.user.UserRegisterRequest;
import com.sprint.mission.discodeit.service.dto.user.UserRegisterResponse;
import com.sprint.mission.discodeit.service.dto.user.UserSearchResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateResponse;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserRegisterResponse registerUser(UserRegisterRequest request);

    UserSearchResponse searchUserById(UUID userId);

    List<UserSearchResponse> searchAllUser();

    UserUpdateResponse updateUserById(UserUpdateRequest request);

    UserDeleteResponse deleteUserById(UUID userId);
}
