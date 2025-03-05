package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<String, UserStatus> userStatusStore = new ConcurrentHashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusStore.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(String userId) {
        return Optional.ofNullable(userStatusStore.get(userId));
    }

    @Override
    public void deleteByUserId(String userId) {
        userStatusStore.remove(userId);
    }
}