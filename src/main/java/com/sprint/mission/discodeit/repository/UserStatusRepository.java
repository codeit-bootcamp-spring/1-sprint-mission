package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findByUser(User user);
    void deleteByUser(User user);
    boolean existsByUser(User user);
    List<UserStatus> findAllByUserIn(List<User> users);
}
