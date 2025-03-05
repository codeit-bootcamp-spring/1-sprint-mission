package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.user.User;

import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    // 유저 저장
    User userSave(User user);

    // 유저 조회
    User findUserById(UUID userId);

    // 모든 유저 조회
    Map<UUID, User> findAllUser();

    // 유저 삭제
    void removeUserById(UUID userId);

    // 유저 이메일로 찾기
    User findUserByEmail(String email);

    // json 초기화
    void clearData();
    void resetData();

    User findUserByName(String userName);
}
