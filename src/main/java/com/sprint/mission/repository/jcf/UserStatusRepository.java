package com.sprint.mission.repository.jcf;

import com.sprint.mission.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserStatusRepository {

    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

    public void save(UserStatus userStatus){
        statusMap.put(userStatus.getUserId(), userStatus);
    }

    public UserStatus findById(UUID userId){
        UserStatus userStatus = statusMap.get(userId);
        if (userStatus == null) throw new NoSuchElementException("Cannot Find status By UserId");
        else return userStatus;
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
