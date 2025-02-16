package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    // 생성
    User createUser(UserCreateDto dto);

    // 정보 수정
    User updateUser(UserUpdateDto dto);

    // 조회
    UserResponseDto findUserById(UUID userId);
    List<UserResponseDto> findAllUsers();

    // 삭제
    void deleteUser(UUID userId);
}
