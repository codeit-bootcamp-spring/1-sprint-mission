package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.enity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // Create
    User create(UserCreateRequest userCreateRequest);  // 유저 생성

    // Read
    UserResponse find(UUID userId);    // 유저 정보 단건 조회

    List<UserResponse> findAll(); // 모든 유저 조회

    // Update
    User update(UUID userId, UserUpdateRequest userUpdateRequest);

    // Delete
    void delete(UUID userId);
}
