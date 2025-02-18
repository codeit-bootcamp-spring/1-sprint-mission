package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    boolean create(UUID id);


    UserStatus findById(UUID id);


    List<UserStatus> findAll();

    boolean update(UUID userId);


    boolean updateByUserId(UUID userId);


    void delete(List<UserStatus> list);

    public List<UUID> getOnlineUsers();
}
