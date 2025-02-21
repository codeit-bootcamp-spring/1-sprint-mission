package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import java.util.Collection;
import java.util.UUID;

public interface UserService {
    UserCreateResponse createUser(UserCreateRequest userCreateRequest, BinaryContentCreateRequest binaryContentCreateRequest);
    // Read : 전체 유저 조회, 특정 유저 조회
    Collection<UserFindResponse> showAllUsers(); //나중? 다음?에 Users 자체를 반환하도록 변경
    UserFindResponse getUserById(UUID id);

    // Update
    void updateUserInfo(UUID userId, UserUpdateRequest userUpdateRequest);

    // Delete : 특정 유저 삭제
    void removeUserById(UUID id);
}
