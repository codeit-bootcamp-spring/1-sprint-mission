package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus>storage = new ConcurrentHashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        storage.put(userStatus.getId(), userStatus);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return storage.values().stream()
                .filter(userStatus -> userStatus.getUser().equals(userId))
                .findFirst();
    }

    @Override //
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(storage.get(userStatusId));
    }

    @Override
    public boolean existsByUser(User user) {
        return storage.values().stream()
                .anyMatch(status ->status.getUser().equals(user));
    }

    @Override
    public boolean existsByUserStatusId(UUID userStatusId) {
        return storage.containsKey(userStatusId);
    }

    @Override
    public boolean existByUserId(UUID userId) {
        return storage.values().stream()
                .anyMatch(status -> status.getUser().getId().equals(userId));
    }

    @Override
    public void delete(UserStatus userStatus) {
        storage.values().removeIf(status -> status.equals(userStatus));
    }

    @Override
    public void deleteById(UUID userStatusId) {
        storage.remove(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.values().removeIf(status -> status.getUser().getId().equals(userId));
    }
}
