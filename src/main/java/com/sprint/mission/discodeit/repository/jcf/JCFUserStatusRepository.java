package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> userStatusData = new HashMap<>();

    @Override
    public UserStatus saveUserStatus(UserStatus userStatus) {
        userStatusData.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findUserStatusById(UUID userStatusId) {
        return userStatusData.get(userStatusId);
    }

    @Override
    public Optional<UserStatus> findUserStatusByUser(User user) {
        return userStatusData.values().stream()
                .filter(userStatus -> userStatus.getUser().equals(user))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAllUserStatuses() {
        return new ArrayList<>(userStatusData.values());
    }

    @Override
    public void removeUserStatus(UUID userStatusId) {
        userStatusData.remove(userStatusId);
    }
}
