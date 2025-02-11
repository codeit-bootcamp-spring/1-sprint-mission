package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> storage = new ConcurrentHashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        storage.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatus updatedStatus) {
        for (Map.Entry<UUID, UserStatus> entry : storage.entrySet()) {
            if (entry.getValue().getUserId().equals(userId)) {
                storage.put(entry.getKey(), updatedStatus);
                return updatedStatus;
            }
        }
        throw new RuntimeException("UserStatus not found for userId: " + userId);
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
