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
    public Optional<UserStatus> findById(UUID userId) {
        return Optional.ofNullable(userStatusData.get(userId));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusData.values());
    }

    @Override
    public void deleteById(UUID userId) {
        userStatusData.remove(userId);
    }

    @Override
    public boolean existsById(UUID userId) {
        return userStatusData.containsKey(userId);
    }
}
