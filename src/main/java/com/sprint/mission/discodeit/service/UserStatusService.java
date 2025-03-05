package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;

import java.util.List;

public interface UserStatusService {

    UserStatusResponseDto findById(String userStatusId);

    List<UserStatusResponseDto> findAll();

    UserStatusResponseDto create(CreateUserStatusDto createUserStatusDto);

    UserStatusResponseDto updateByUserId(String userStatusId, UpdateUserStatusDto updateUserStatusDto);

    boolean delete(String userStatusId);
}
