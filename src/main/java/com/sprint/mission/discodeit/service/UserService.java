package com.sprint.mission.discodeit.service;

import java.util.List;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    // 생성
    User createUser(String username, String password, String email);

    // 정보 수정
    User updateUserName(User user, String username);
    User updatePassword(User user, String password);
    User updateEmail(User user, String email);

    // 조회
    User findUserById(User u);
    List<User> findAllUsers();

    // 유저 프린트
    void printUser(User user);
    void printListUsers(List<User> users);

    // 삭제
    void deleteUserById(User user);
}
