package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository { //crud
    //유저 생성
    User createUser (String name, String email);
    //유저 조회 - ALL
    List<User> getAllUserList();

    //유저 ID로 조회
    User searchById(UUID userId);

    //유저 정보 수정 -> name, email 정보 수정
    void updateUserName(UUID userId, String name);
    void updateUserEmail(UUID userId, String email);

    //유저 삭제
    void deleteUser(UUID userId);
}
