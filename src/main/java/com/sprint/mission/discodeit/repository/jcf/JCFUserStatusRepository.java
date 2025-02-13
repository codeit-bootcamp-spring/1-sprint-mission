package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;

@Repository("jcfUserStatusRepository")
public class JCFUserStatusRepository implements UserStatusRepository {

    private final ConcurrentHashMap<UUID, UserStatus> data = new ConcurrentHashMap<>();

    @Override
    public void save(UserStatus status) {
        data.put(status.getUserId(), status);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
