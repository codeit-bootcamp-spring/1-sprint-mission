package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusRequest userStatusRequest); //새로운 생성

  void saveExist(UserStatus userStatus); //존재하는 상태 저장(userCreate시 연동)

  UserStatusDto find(UUID uuid);

  List<UserStatusDto> findAll();

  UserStatusDto update(UUID id, UserStatusUpdateRequest userStatusUpdateRequest);

  UserStatus updateByUserId(UUID userID, UserStatusUpdateRequest userStatusUpdateRequest);

  void delete(UUID uuid);

}
