package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.addOn.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserStatusRepository {

    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

    public void save(UserStatus userStatus){
        statusMap.put(userStatus.getUserId(), userStatus);
    }

    public Optional<UserStatus> findById(UUID userId){
        return Optional.of(statusMap.get(userId));
    }

    public List<UserStatus> findAll(){
        return new ArrayList<>(statusMap.values());
    }

    public Boolean isExistById(UUID userId){
        return statusMap.containsKey(userId);
    }

    public void delete(UUID userId) {
        statusMap.remove(userId);
    }
}
