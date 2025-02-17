package com.sprint.mission.discodeit.user.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.user.dto.request.CreateUserStatusRequest;
import com.sprint.mission.discodeit.user.dto.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.user.entity.UserStatus;

public interface UserStatusService {
	UserStatus create(CreateUserStatusRequest request);

	UserStatus find(UUID id);

	List<UserStatus> findAll();

	UserStatus update(UpdateUserStatusRequest request);

	UserStatus updateByUserId(UpdateUserStatusRequest request);

	void deleteByUserId(UUID id);
}
