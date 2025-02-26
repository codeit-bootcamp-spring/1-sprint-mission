package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> profileCreateDto); //사용자 생성
    UserDto findById(UUID userId); //사용자 단일 조회
    List<UserDto> findAll(); //사용자 모두 조회
    User update(UUID userId,UserUpdateDto userUpdateDto,Optional<BinaryContentCreateDto> profileCreateDto);//사용자 수정
    void deleteById(UUID userId); //사용자 삭제
}
