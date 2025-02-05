package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.entity.AccountStatus;

import java.time.Instant;
import java.util.List;

public interface UserService {

    //생성
    CreateUserDto create(String nickname, String email, String password);

    //모두 읽기
    List<CreateUserDto> getUsers();

    //읽기
    //단건 조회 - UUID
    CreateUserDto getUserByUUID(String userId);

    //단건 조회 - 이메일로 조회
    CreateUserDto getUserByEmail(String email);

    //다건 조회 - 닉네임
    List<CreateUserDto> getUsersByNickname(String nickname);

    //다건 조회 - 계정 상태
    List<CreateUserDto> getUsersByAccountStatus(AccountStatus accountStatus);

    //다건 조회 - 사용자 상태
    //List<UserDTO> getUserByUserStatus(UserStatus userStatus);

    //수정
    CreateUserDto updateUser(String userId, CreateUserDto createUserDto, Instant updatedAt);

    //삭제
    boolean deleteUser(String userId);


}
