package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.User;

import java.util.List;

public interface UserService {

    //생성
    public boolean createUser(String nickname, String email);

    //모두 읽기
    public List<User> getUsers();

    //읽기
    //단건 조회 - UUID
    public User getUserByUUID(String userId);

    //단건 조회 - 이메일로 조회
    public User getUserByEmail(String email);

    //다건 조회 - 닉네임
    public List<User> getUsersByNickname(String nickname);

    //다건 조회 - 계정 상태
    public List<User> getUsersByAccountStatus(AccountStatus accountStatus);

    //수정
    public boolean updateUser(User user);

    //삭제
    public boolean deleteUser(User user);
}
