package com.sprint.mission.discodeit.repository.userstatus;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.repository.userstatus.interfaces.UserStatusRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserStatusInMemoryRepository implements UserStatusRepository {

    private final Map<User, UserStatus> userStatuses = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        User user = userStatus.getUser();
        userStatuses.put(user, userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUser(User user) {
        return Optional.ofNullable(userStatuses.get(user));
    }
}
