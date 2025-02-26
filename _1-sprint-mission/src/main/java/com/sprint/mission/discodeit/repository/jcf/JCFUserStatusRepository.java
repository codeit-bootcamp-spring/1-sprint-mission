package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusData;
    public JCFUserStatusRepository() {
        this.userStatusData = new HashMap<>();
    }
    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusData.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusData.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(us->us.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusData.values());
    }

    @Override
    public void deleteById(UUID id) {
        userStatusData.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findByUserId(userId).ifPresent(userStatus -> userStatusData.remove(userId));
    }

    @Override
    public boolean existsById(UUID userId) {
        return userStatusData.containsKey(userId);
    }
}
