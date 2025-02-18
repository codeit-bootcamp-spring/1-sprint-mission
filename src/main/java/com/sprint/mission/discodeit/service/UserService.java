package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.userDto.FindUserResponseDto;
import com.sprint.mission.discodeit.dto.userDto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    // 생성
    UUID create(CreateUserRequestDto createUserDto) throws IOException;

    // 읽기
    FindUserResponseDto find(UUID id);

    // 모두 읽기
    List<FindUserResponseDto> findAll();

    // 수정
    void updateUser(UUID id, UpdateUserRequestDto updateUserRequestDto) throws IOException;

    // 삭제
    void delete(UUID id);

    // 유저 존재 여부 확인
    void userIsExist(UUID id);
}
