package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID userStatusId);
    List<UserStatus> findAll();
    void deleteById(UUID userStatusId);
    boolean existsById(UUID userStatusId);
}
