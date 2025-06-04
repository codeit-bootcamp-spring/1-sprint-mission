package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;

public interface UserService {

	UserDto create(UserCreateRequest userCreateRequest,
		Optional<BinaryContentCreateRequest> profileCreateRequest);

	UserDto find(UUID userId);

	List<UserDto> findAll();

	UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
		Optional<BinaryContentCreateRequest> profileCreateRequest);

	void delete(UUID userId);
}
