package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    public abstract UUID createUser(String nickname);

    // Read : 전체 유저 조회, 특정 유저 조회
    public abstract int showAllUsers();
    public abstract User getUserById(UUID id);

    // Update : 특정 유저 닉네임 변경
    public abstract void updateUserNickname(UUID id);

    // Delete : 전체 유저 삭제, 특정 유저 삭제
    public abstract void clearAllUsers();
    public abstract void removeUserById(UUID id);

}
