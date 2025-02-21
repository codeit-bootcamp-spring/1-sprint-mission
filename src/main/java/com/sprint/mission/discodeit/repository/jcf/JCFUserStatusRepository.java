package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> store = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        store.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(store.get(userId));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.remove(userId);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return store.containsKey(userId);
    }
}
