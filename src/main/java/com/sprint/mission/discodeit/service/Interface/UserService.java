package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDTO;
import com.sprint.mission.discodeit.dto.user.UserResponseDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    //생성
    User createUser(UserCreateRequestDTO request);

    //읽기
    Optional<UserResponseDTO> getUserById(UUID id);

    //모두 읽기
    List<UserResponseDTO> getAllUsers();

    //수정
    User updateUser(UserUpdateRequestDTO request);

    //삭제
    void deleteUser(UUID id);
}

