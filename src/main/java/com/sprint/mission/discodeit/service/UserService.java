package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import java.util.Collection;
import java.util.UUID;

public interface UserService {
    UUID createUser(UserCreateRequest userCreateRequest);
    // Read : 전체 유저 조회, 특정 유저 조회
    Collection<UserFindResponse> showAllUsers(); //나중? 다음?에 Users 자체를 반환하도록 변경
    UserFindResponse getUserById(UUID id);

    // Update
    void updateUserInfo(UserUpdateRequest userUpdateRequest);

    // Delete : 특정 유저 삭제
    void removeUserById(UUID id);
}
