package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {
	UserResponse createUser(CreateUserRequest requestDto);

	UserResponse findUser(UUID existUserId);

	List<UserResponse> findAllUsers();

	User updateUser(UUID existUserId, UpdateUserRequest requestDto);

	void deleteUser(UUID userId);
}
