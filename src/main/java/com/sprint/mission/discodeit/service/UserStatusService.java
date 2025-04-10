package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import java.util.List;

public interface UserStatusService {

  UserStatusDto findById(String userStatusId);

  UserStatusDto findByUserId(String userId);

  List<UserStatusDto> findAll();

  UserStatusDto create(CreateUserStatusDto createUserStatusDto);

  UserStatusDto updateByUserId(String userStatusId,
      UserStatusUpdateRequest userStatusUpdateRequest);

  boolean delete(String userStatusId);
}
