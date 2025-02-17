package com.sprint.mission.discodeit.user.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.user.dto.request.CreateUserRequest;
import com.sprint.mission.discodeit.user.dto.request.UpdateUserRequest;
import com.sprint.mission.discodeit.user.entity.User;

public interface UserService {
	User createUser(CreateUserRequest request);

	User findUser(UUID existUserId);

	List<User> findAllUsers();

	User updateUser(UUID existUserId, UpdateUserRequest request);

	void deleteUser(UUID userId);
}
