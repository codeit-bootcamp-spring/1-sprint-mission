package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> store;

    public JCFUserStatusRepository() {
        store = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        store.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return store.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }
    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
    }

}
