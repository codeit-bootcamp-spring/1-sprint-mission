package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.AccountStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {

    //생성
    UserResponseDto create(CreateUserDto createUserDto);

    //모두 읽기
    List<User> findAll();

    //읽기
    //단건 조회 - UUID
    UserResponseDto findById(String userId);

    //단건 조회 - 이메일로 조회
    UserResponseDto findByEmail(String email);

    //다건 조회 - 닉네임
    List<UserResponseDto> findAllContainsNickname(String nickname);

    //다건 조회 - 계정 상태
    List<User> findAllByAccountStatus(AccountStatus accountStatus);

    //다건 조회 - 사용자 상태
    //List<User> getUserByUserStatus(UserStatus userStatus);

    //수정
    User updateUser(String userId, UpdateUserDto updateUserDto);

    //삭제
    boolean deleteUser(String userId);
    
}
