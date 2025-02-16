package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Scope("singleton")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
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
    public UserStatus find(UUID userStatusId) {
        return data.get(userStatusId);
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID userStatusId) {
        data.remove(userStatusId);
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        return data.containsKey(userStatusId);
    }
}
