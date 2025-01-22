package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // 유저 생성 (username과 password로 생성)
    User createUser(String username, String password);

    // 유저 단건 조회 (ID로 조회)
    User getUserById(UUID userId);

    // 유저 다건 조회 (모든 유저)
    List<User> getAllUsers();

    // 유저 이름 수정
    User updateUsername(UUID userId, String newUsername);

    //유저 비번 수정
    User updatePassword(UUID userId, String newPassword);

    // 유저 삭제
    boolean deleteUser(UUID userId);
}


