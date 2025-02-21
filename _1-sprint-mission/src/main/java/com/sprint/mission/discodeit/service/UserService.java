package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.ResponseUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserDto userDto); //사용자 생성
    ResponseUserDto findById(UserDto userDto); //사용자 단일 조회
    List<ResponseUserDto> findAll(); //사용자 모두 조회
    User update(UserDto userDto);//사용자 수정
    void deleteById(UUID userId); //사용자 삭제
}
