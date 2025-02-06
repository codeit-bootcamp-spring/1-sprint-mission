package com.sprint.mission.discodeit.repository.domain.file;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.List;
import java.util.UUID;

public class UserStatusFileRepositoryImpl implements UserStatusRepository {
    @Override
    public UserStatus save(UserStatusDto userStatusDto) throws IllegalAccessException {
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
    public List<UserStatus> findAll() {
        return List.of();
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public void update(UserStatusDto userStatusDto) {

    }

    @Override
    public void updateByUserId(UUID id) {

    }
}
