package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.data.UserStatusData;

import java.util.Map;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {

    private final UserStatusData userStatusData = UserStatusData.getInstance();

    @Override
    public void save(UserStatus userStatus) {
        userStatusData.put(userStatus);
    }

    @Override
    public Map<UUID, UserStatus> load() {
        return userStatusData.load();
    }

    @Override
    public void delete(UUID id) {
        userStatusData.delete(id);
    }
}
