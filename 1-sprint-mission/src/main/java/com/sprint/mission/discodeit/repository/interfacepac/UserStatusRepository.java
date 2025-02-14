package com.sprint.mission.discodeit.repository.interfacepac;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    void save(UserStatus userStatus); // 새로운 사용자 상태 저장
    //
    Optional<UserStatus> findByUserId(UUID userId); //사용자 상태 조회
    Optional<UserStatus> findById(UUID userStatusId); //사용자 상태 조회

    //
    boolean existsByUser(User user);
    boolean existsByUserStatusId(UUID userStatusId);
    boolean existByUserId(UUID userId);
    //
    void delete(UserStatus userStatus); // 사용자 삭제 할때 사용자 상태 삭제
    void deleteById(UUID userStatusId);
    void deleteByUserId(UUID userId);
}
