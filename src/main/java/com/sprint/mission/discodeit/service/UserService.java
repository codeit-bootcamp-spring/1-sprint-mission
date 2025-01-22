package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 새로운 유저 생성
    User createUser(String name, String email);

    // 유저 정보 수정 - name 과 email 만 수정 가능
    void updateUserName(User user, String name);
    void updateUserEmail(User user, String email);

    // 모든 유저 조회
    List<User> getAllUserList();
    User searchById(UUID userId);

    // 유저 정보 전체 리스트로 출력
    void printUserInfo(User user);
    void printUserListInfo(List<User> userList);

    // 유저 삭제
    void deleteUser(User user);

}
