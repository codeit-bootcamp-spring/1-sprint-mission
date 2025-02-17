package com.sprint.mission.discodeit.repository.data;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// UserStatus 데이터만 관리하는 일급 컬렉션
public class UserStatusData {
    private final Map<UUID, UserStatus> userStatuses = new ConcurrentHashMap<>();

    private static UserStatusData userStatusData;

    private UserStatusData() {
    }

    public static UserStatusData getInstance() {

        if (userStatusData == null) {
            userStatusData = new UserStatusData();
        }

        return userStatusData;
    }

    public void put(UserStatus userStatus) {

        userStatuses.put(userStatus.getId(), userStatus);
    }

    public Map<UUID, UserStatus> load() {

        return userStatuses;
    }

    public void delete(UUID id) {

        userStatuses.remove(id);
    }
}
