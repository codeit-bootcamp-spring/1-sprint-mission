package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    //사용자 가입
    void registerUser(User user);
    //사용자 이름 정보 수정
    void updateUserName(User user, String name);
    //사용자 이메일 정보 수정
    void updateUserEmail(User user, String email);
    //사용자 탈퇴
    void deleteUser(String name, String password);
    //사용자 정보 확인
    void getUserInfo(String name);
}
