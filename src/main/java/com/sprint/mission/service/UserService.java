package com.sprint.mission.service;

import com.sprint.mission.entity.User;

import java.util.List;

public interface UserService {

    // 유저 생성
    User createUser(String email, String name);

    // 이메일 변경
    void updateMail(User user, String name);

    // 모든 유저 조회, 특정 유저 조회
    List<User> findAllUserList();
    void searchUser(User user);

    // 유저 삭제
    void deleteUser(User user);
}
