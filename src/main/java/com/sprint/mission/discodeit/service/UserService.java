package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserEntity;

import java.util.UUID;

public interface UserService {

    // 유저 추가
    void addUser(UserEntity user);

    // 유저 호출
    UserEntity getUserById(UUID id);

    // 유저 호출 (DelFlg 검증)
    UserEntity getUserByIdWithFlg(UUID id);

    // 유저명 갱신
    void updateUsername(UUID id, String username);

    // email 갱신
    void updateEmail(UUID id, String email);

    // 유저명 및 email 갱신
    void updateUser(UUID id, String username, String email);

    // 유저 삭제
    void deleteUserByUserId(UUID id);

    // 유저 삭제(Flg Update 방식)
    void deleteUserByUserIdWithFlg(UUID id);

}
