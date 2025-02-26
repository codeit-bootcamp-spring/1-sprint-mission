package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {

    private Map<UUID, UserStatus> storage = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        if (userStatus == null) {
            throw new IllegalArgumentException("UserStatus가 없음");
        }
        storage.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID userId) {
        userIdChecker(userId);
        UserStatus userStatus = storage.get(userId);
//        if (userStatus == null) {
//            throw new IllegalStateException("유저가 존재하지 않음 = " + userId);
//        }
        return userStatus;
    }

    @Override
    public UserStatus findByUserName(String username) {
        return null;
    }

    @Override
    public UserStatus updateState(String userName, UserStatus userStatus) {
        return null;
    }

    @Override
    public Map<UUID, UserStatus> findAll() {
        return Map.copyOf(storage);
    }

    @Override
    public UserStatus updateState(UUID userId, UserStatus userStatus) {
        if (userId == null || userStatus == null) {
            throw new IllegalArgumentException("UserId 또는 UserStatus가 없음");
        }
        isContainUserIdInStorage(userId);
        storage.put(userId, userStatus);
        return userStatus;
    }

    @Override
    public void remove(UUID userId) {
        userIdChecker(userId);
        isContainUserIdInStorage(userId);
        storage.remove(userId);
    }

    private void isContainUserIdInStorage(UUID userId) {
        if (!storage.containsKey(userId)) {
            throw new IllegalStateException("UserStatus not found for userId: " + userId);
        }
    }

    private static void userIdChecker(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId가 없음");
        }
    }

    @Override
    public void clearData() {
        this.storage.clear();
    }

    @Override
    public void resetData() {
        this.storage = new HashMap<>();
    }
}