package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatuses = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatuses.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatuses.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatuses.values());
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        for (UserStatus status : userStatuses.values()) {
            if (status.getUserId().equals(userId)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public boolean existsById(UUID id) {
        return userStatuses.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        userStatuses.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        List<UUID> toDelete = new ArrayList<>();
        for (Map.Entry<UUID, UserStatus> entry : userStatuses.entrySet()) {
            if (entry.getValue().getUserId().equals(userId)) {
                toDelete.add(entry.getKey());
            }
        }
        for (UUID id : toDelete) {
            userStatuses.remove(id);
        }
    }
}