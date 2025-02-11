package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() { this.data = new HashMap<>(); }

    @Override
    public UserStatus findByUserId(String userid) {
        return null;
    }

    @Override
    public void save(UserStatus userStatus) {
        data.put(userStatus.getId() ,new UserStatus(userStatus.getUserid()));
    }
}
