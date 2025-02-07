package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;


public class JCFUserStatusRepository implements UserStatusRepository {
    //<UserStatus id, UserStatus>
    private final HashMap<UUID, UserStatus> userStatusStore = new HashMap<>();
    //<User id, UserStatus id>
    private final HashMap<UUID, UUID> userToStatusMap = new HashMap<>();


    @Override
    public void save(UserStatus userStatus) {
        userStatusStore.put(userStatus.getId(), userStatus);
        userToStatusMap.put(userStatus.getUserId(), userStatus.getId());
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusStore.get(id);
    }

    @Override
    public UserStatus findByUserId(UUID userId){
        UUID id= userToStatusMap.get(userId);
        return userStatusStore.get(id);
    }

    @Override
    public HashMap<UUID, UserStatus> findAll() {
        return new HashMap<>(userStatusStore);
    }

    @Override
    public void delete(UUID id) {
        userStatusStore.remove(id);
    }
}
