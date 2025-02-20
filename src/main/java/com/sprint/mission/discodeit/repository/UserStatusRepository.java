package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusRepository {
    // 처음 활동 상태 저장
    boolean save(UserStatus userStatus);

    // 바뀐 활동 상태 저장
    boolean updateOne(UserStatus userStatus);

    // 모든 도메인 정보 삭제
    void deleteAllByUserId(UUID userId);
}
