package com.sprint.mission.discodeit.user.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.user.entity.User;

public interface UserService {
	User createUser(UserCreateRequest request, Optional<BinaryContentCreateRequest> profileCreateRequest);

	UserResponse findUser(UUID existUserId);

	List<UserResponse> findAllUsers();

	User update(UUID userId, UserUpdateRequest userUpdateRequest,
		Optional<BinaryContentCreateRequest> profileCreateRequest);

	void delete(UUID userId);
}
