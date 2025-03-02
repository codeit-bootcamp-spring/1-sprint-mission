package com.sprint.mission.discodeit.user.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.user.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.user.entity.UserStatus;

public interface UserStatusService {
	UserStatus create(UserStatusCreateRequest request);

	UserStatus find(UUID id);

	List<UserStatus> findAll();

	UserStatus update(UUID userStatusId, UserStatusUpdateRequest request);

	UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request);

	void delete(UUID id);
}
