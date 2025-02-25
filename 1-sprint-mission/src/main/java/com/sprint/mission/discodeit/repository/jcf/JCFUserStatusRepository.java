package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class JCFUserStatusRepository {
    private final List<UserStatus> statuses = new ArrayList<>();

    public void saveStatus(UUID uid){
        UserStatus userStatus = new UserStatus();
        userStatus.setId(uid);
        userStatus.setCreatedAt(Instant.now());
        userStatus.setUpdatedAt(Instant.now());
        userStatus.setUserOnOff(true);

        statuses.add(userStatus);
    }

    public void updateStatus(UUID uid){
        for(UserStatus userStatus : statuses){
            if(userStatus.getId().equals(uid)){
                userStatus.setUpdatedAt(Instant.now());
                return;
            }
        }
    }

    public void delete(UUID uid){
        statuses.removeIf(status -> status.getId().equals(uid));
    }

    public void checkOnline(UUID uid){
        for(UserStatus userStatus : statuses){
            if(userStatus.getId().equals(uid)){
                if(userStatus.getUpdatedAt().plus(5, ChronoUnit.MINUTES).compareTo(Instant.now()) > 0){
                    userStatus.setUserOnOff(false);
                }
            }
        }
    }

    public boolean find(UserDto user) {
        for(UserStatus status : statuses){
            if(status.getId().equals(user.getId())){
                return status.isUserOnOff();
            }
        }

        return false;
    }

    public UserStatus findByUserId(UserDto user){
        for(UserStatus status : statuses){
            if(status.getId().equals(user.getId())){
                return status;
            }
        }

        return null;
    }

    public List<UserStatus> findAll(){
        return statuses;
    }
}
