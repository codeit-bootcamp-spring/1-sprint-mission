package com.sprint.mission.discodeit.repository.domain;

import com.sprint.mission.discodeit.domain.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//유저의 상태
@Repository
public interface UserStatusRepository {
    void save(UserStatus userStatus);
    UserStatus getUserStatus(UUID id); //유저 상태
    List<UserStatus> getUserStatusList(); //모든 유저 상태
    void deleteUserStatus(UUID id);
    void updateUserStatus(); //유저 상태 업데이트
}
