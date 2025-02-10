package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
	private final UserStatusRepository userStatusRepository;
	private final UserRepository userRepository;

	@Override
	public UserStatus create(CreateUserStatusRequest request) {
		// User 존재 확인
		userRepository.findById(request.userId())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// 중복 체크
		if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
			throw new IllegalArgumentException("UserStatus already exists");
		}

		UserStatus userStatus = new UserStatus(
			request.userId(),
			request.lastActiveAt()
		);
		return userStatusRepository.save(userStatus);
	}

	@Override
	public UserStatus find(UUID id) {
		return userStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
	}

	@Override
	public List<UserStatus> findAll() {
		return userStatusRepository.findAllOnlineUsers();
	}

	@Override
	public UserStatus update(UUID id, UpdateUserStatusRequest request) {
		UserStatus userStatus = userStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

		userStatus.updateLastActiveTime();
		return userStatusRepository.save(userStatus);
	}

	@Override
	public UserStatus updateByUserId(UUID userId, UpdateUserStatusRequest request) {
		UserStatus userStatus = userStatusRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

		userStatus.updateLastActiveTime();
		return userStatusRepository.save(userStatus);
	}

	@Override
	public void delete(UUID id) {
		userStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
		userStatusRepository.deleteByUserId(id);
	}
}
