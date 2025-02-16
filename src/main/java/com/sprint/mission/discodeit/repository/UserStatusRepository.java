package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserStatusRepository {
    void createUserStatus(UserStatus userStatus);
    void findUserStatus(UUID id);

}
