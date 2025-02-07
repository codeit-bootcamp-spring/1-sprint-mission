package com.sprint.mission.discodeit.repository.domain.jcf;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserStatusJcfRepositoryImpl implements UserStatusRepository {
    private Map<UUID, UserStatus> userStatusMap = new HashMap<>();


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
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public void delete(UUID id) {
        userStatusMap.remove(id);
    }

    @Override
    public void update(UserStatusDto userStatusDto) { //유저 상태 필드 변경
        UserStatus updated = findById(userStatusDto.id()).updateFields(userStatusDto);
        userStatusMap.replace(updated.getId(), updated);
    }

}
