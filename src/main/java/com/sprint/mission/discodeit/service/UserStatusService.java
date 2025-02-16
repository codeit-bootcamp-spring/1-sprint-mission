package com.sprint.mission.discodeit.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusService {
	UserStatus create(CreateUserStatusRequest request);

	UserStatus find(UUID id);

	List<UserStatus> findAll();

	UserStatus update(UUID id, UpdateUserStatusRequest request);

	UserStatus updateByUserId(UUID userId, Instant lastActiveAt);

	void deleteByUserId(UUID id);
}
