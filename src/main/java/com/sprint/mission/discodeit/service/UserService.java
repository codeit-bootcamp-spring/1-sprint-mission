package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public interface UserService {

    // 유저서비스에 유저 추가
    void userServiceAdd(User user);

    // 조회
    User getUser(UUID id);

    // 모두 읽기
    HashMap<UUID, User> getAllUsers();

    // 수정

    // 사용자 수정
    void updateUser(UUID uuID, String email, String iD, String password);

    // 삭제
    void deleteUser(User user);
}
