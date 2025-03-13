package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatus create(UserStatusCreateDTO userStatusCreateDTO); //새로운 생성
    void saveExist(UserStatus userStatus); //존재하는 상태 저장(userCreate시 연동)
    
    UserStatus find(UUID uuid);

    List<UserStatus> findAll();

    UserStatus update(UUID id, UserStatusUpdateDTO userStatusUpdateDTO);

    UserStatus updateByUserId(UUID userID, UserStatusUpdateDTO userStatusUpdateDTO);

    void delete(UUID uuid);

}
