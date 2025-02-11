package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.Map;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {
    @Override
    public UserStatus save(UserStatus userStatus) {
        return null;
    }

    @Override
    public UserStatus findById(UUID id) {
        return null;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return null;
    }

    @Override
    public Map<UUID, UserStatus> load() {
        return Map.of();
    }

    @Override
    public Boolean existsByUserId(UUID userId) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}