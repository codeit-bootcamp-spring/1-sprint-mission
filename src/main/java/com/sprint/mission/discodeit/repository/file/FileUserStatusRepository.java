package com.sprint.mission.discodeit.repository.file;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
	@Override
	public UserStatus save(UserStatus userStatus) {
		return null;
	}

	@Override
	public Optional<UserStatus> findById(UUID id) {
		return Optional.empty();
	}

	@Override
	public Optional<UserStatus> findByUserId(UUID userId) {
		return Optional.empty();
	}

	@Override
	public List<UserStatus> findAllOnlineUsers() {
		return List.of();
	}

	@Override
	public void deleteByUserId(UUID userId) {

	}
}
