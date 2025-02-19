package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);

    UserStatus findById(UUID id);

    List<UserStatus> findAll();

    UserStatus findByUserId(UUID userId);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    void deleteByUserId(UUID userId);
}

