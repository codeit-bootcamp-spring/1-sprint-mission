package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusMap;
    JCFUserStatusRepository(){
        userStatusMap=new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        if(userStatus==null){
            throw new IllegalArgumentException("UserStatus is null.");
        }
        userStatusMap.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is null.");
        }
        UserStatus userStatus = userStatusMap.get(id);
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Id is null.");
        }
        return userStatusMap.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(()->new NullPointerException ("No found"));

    }

    @Override
    public Map<UUID, UserStatus> load() {
        return userStatusMap;
    }

    @Override
    public Boolean existsByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Id is null.");
        }
        return userStatusMap.values().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }

    @Override
    public void delete(UUID id) {
        userStatusMap.remove(id);
    }
}