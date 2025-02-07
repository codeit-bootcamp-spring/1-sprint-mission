package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return data.values().stream()
                .anyMatch(status -> status.getUserId().equals(userId));
    }

    @Override
    public void deleteByuserId(UUID userId) {
        data.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }
}
