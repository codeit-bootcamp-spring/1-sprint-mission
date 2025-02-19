package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID,UserStatus> data;

    public JCFUserStatusRepository() {
        data = new HashMap<>();
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
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findAny();
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        for (UUID id : data.keySet()) {
            if (data.get(id).getUserId().equals(userId)) {
                data.remove(id);
                break;
            }
        }
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findByUserId(userId).isPresent();
    }
}
