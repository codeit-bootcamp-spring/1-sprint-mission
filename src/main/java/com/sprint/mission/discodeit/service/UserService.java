package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface UserService  {
    //도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요
    UserDto createUser(UserDto userDto);
    UserDto getUser(UUID id);
    List<UserDto> getUsers();
    void updateUser(UserDto userDto);
    void deleteUser(UUID id);

}
