package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserStatusJcfRepositoryImpl implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusMap = new HashMap<>();


    @Override
    public UserStatus save(UserStatusDto userStatusDto){
        UserStatus userStatus = new UserStatus(userStatusDto.userId());
        userStatusMap.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        UserStatus userStatus = userStatusMap.get(id);
        userStatus = userStatus.checkStatus(); //Status : OFFLINE, ONLINE 다시 체크
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID userId) { //Null 가능성 존재
        UserStatus findUserStatus = userStatusMap.values().stream().filter(userStatus -> userStatus.getUserId().equals(userId)).findFirst().orElse(null);
        if( findUserStatus != null ) findUserStatus = findUserStatus.checkStatus(); //Status : OFFLINE, ONLINE 다시 체크
        return findUserStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        for (UserStatus userStatus : userStatusMap.values()) {
            userStatus.checkStatus();
            System.out.println("userStatus = " + userStatus);
        }
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public void delete(UUID id) {
        userStatusMap.remove(id);
    }

    @Override
    public void update(UserStatusDto before, UserStatusDto after) { //유저 상태 필드 변경
        UserStatus updated = findById(before.id()).updateFields(after);
        userStatusMap.replace(updated.getId(), updated);
    }


}
