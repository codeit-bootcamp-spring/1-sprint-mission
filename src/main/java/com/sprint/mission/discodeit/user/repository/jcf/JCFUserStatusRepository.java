package com.sprint.mission.discodeit.user.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.user.entity.UserStatus;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;

public class JCFUserStatusRepository implements UserStatusRepository {
	private final Map<UUID, UserStatus> userStatusData = new HashMap<>();

	@Override
	public UserStatus save(UserStatus userStatus) {
		userStatusData.put(userStatus.getId(), userStatus);
		return userStatus;
	}

	@Override
	public Optional<UserStatus> findById(UUID id) {
		return Optional.ofNullable(userStatusData.get(id));
	}

	@Override
	public Optional<UserStatus> findByUserId(UUID userId) {
		for (UserStatus status : userStatusData.values()) {
			if (status.getUserId().equals(userId)) {
				return Optional.of(status);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<UserStatus> findAllOnlineUsers() {
		List<UserStatus> onlineUsers = new ArrayList<>();
		for (UserStatus status : userStatusData.values()) {
			if (status.isOnline()) {
				onlineUsers.add(status);
			}
		}
		return onlineUsers;
	}

	@Override
	public void deleteByUserId(UUID userId) {
		userStatusData.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
	}
}
