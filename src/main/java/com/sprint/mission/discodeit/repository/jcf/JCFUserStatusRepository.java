package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


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
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusStore.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId){
        UUID id= userToStatusMap.get(userId);
        return Optional.ofNullable(userStatusStore.get(id));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusStore.values());
    }

    @Override
    public void delete(UUID id) {
        userStatusStore.remove(id);
    }
}
