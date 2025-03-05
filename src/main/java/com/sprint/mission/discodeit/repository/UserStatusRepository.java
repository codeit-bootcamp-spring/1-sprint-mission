package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.user.UserStatus;

import java.util.Map;
import java.util.UUID;

public interface UserStatusRepository {

    // userStat 생성
    UserStatus save(UserStatus userStatus);

    // 유저 아이디로 조회
    UserStatus findById(UUID userId);

    // 유저 이름으로 조회
    UserStatus findByUserName(String username);

    // 모든 객체 조회
    Map<UUID, UserStatus> findAll();

    // userId로 찾아서 userStatus의 값으로 수정
    UserStatus updateState(UUID userId, UserStatus userStatus);

    UserStatus updateState(String userName, UserStatus userStatus);

    // 유저 아이디로 삭제
    void remove(UUID userId);

    // 데이터 초기화 메서드 추가
    void clearData();
    void resetData();
}
