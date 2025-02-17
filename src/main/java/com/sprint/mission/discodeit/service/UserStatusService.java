package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusService {
	UserStatus create(CreateUserStatusRequest request);

	UserStatus find(UUID id);

	List<UserStatus> findAll();

	UserStatus update(UpdateUserStatusRequest request);

	UserStatus updateByUserId(UpdateUserStatusRequest request);

	void deleteByUserId(UUID id);
}
