package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus); // 새로운 사용자 상태 저장
    //
    Optional<UserStatus> findByUser(User user); //사용자 상태 조회
    Optional<UserStatus> findById(UUID userStatusId); //사용자 상태 조회

    //
    boolean existsByUser(User user);
    boolean existsById(UUID userStatusId);
    //
    void delete(UserStatus userStatus); // 사용자 삭제 할때 사용자 상태 삭제
    void deleteById(UUID userStatusId);
}
