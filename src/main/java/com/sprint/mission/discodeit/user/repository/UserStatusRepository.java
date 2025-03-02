package com.sprint.mission.discodeit.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.user.entity.UserStatus;

public interface UserStatusRepository {

	UserStatus save(UserStatus userStatus);

	Optional<UserStatus> findById(UUID id);

	Optional<UserStatus> findByUserId(UUID userId);

	List<UserStatus> findAll();

	boolean existsById(UUID id);

	void deleteById(UUID id);

	void deleteByUserId(UUID userId);
}
