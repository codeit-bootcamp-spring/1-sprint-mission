package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    // 생성
    UUID create(CreateUserRequestDto createUserDto, MultipartFile profileImageFile) throws IOException;

    // 읽기
    User find(UUID id);

    // 모두 읽기
    List<UserDto> findAll();

    // 수정
    void updateUser(UUID id, UpdateUserRequestDto updateUserRequestDto, MultipartFile profileImageFile) throws IOException;

    // 삭제
    void delete(UUID id);
}