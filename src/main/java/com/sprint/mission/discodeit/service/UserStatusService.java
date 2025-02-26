package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus createUserStatus(UUID userId);

  UserStatus findUserStatusById(UUID userStatusId);

  List<UserStatus> findAllUserStatuses();

  UserStatus updateUserStatusById(UpdateUserStatusByIdRequest updateUserStatusByIdRequest);

  UserStatusDto updateUserStatusByUserId(UUID userId,
      UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest);

  void deleteUserStatus(UUID userStatusId);
}
