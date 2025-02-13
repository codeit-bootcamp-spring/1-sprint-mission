package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.List;

public interface UserStatusService {

    UserStatus findById(String userStatusId);

    List<UserStatus> findAll();

    UserStatus create(CreateUserStatusDto createUserStatusDto);

    UserStatus updateByUserId(String userStatusId, UpdateUserStatusDto updateUserStatusDto);

    boolean delete(String userStatusId);
}
