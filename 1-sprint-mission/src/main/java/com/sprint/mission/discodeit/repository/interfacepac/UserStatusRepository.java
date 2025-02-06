package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;

public interface UserStatusRepository {
    Optional<UserStatus> findByUser(User user); //사용자 상태 조회
    void save(UserStatus userStatus); // 새로운 사용자 상태 저장
    void delete(UserStatus userStatus); // 사용자 삭제 할때 사용자 상태 삭제
}
