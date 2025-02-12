package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.LoginUserRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;

public class BasicAuthService implements AuthService {
	private final UserRepository userRepository;
	private final UserStatusRepository userStatusRepository;

	public BasicAuthService(UserRepository userRepository, UserStatusRepository userStatusRepository) {
		this.userRepository = userRepository;
		this.userStatusRepository = userStatusRepository;
	}

	@Override
	public UserResponse login(LoginUserRequest request) {
		User user = userRepository.findByUserid(request.userId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid userid or password"));
		if (!validatePassword(request.password(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid userid or password");
		}
		UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid userid or password"));
		//status 온라인으로 바꿔야되는 로직 추가해야된다...
		userStatusRepository.save(userStatus);

		return createLoginResponse(user, userStatus);
	}

	private boolean validatePassword(String inputPassword, String storedPassword) {
		// 실제 구현에서는 암호화된 비밀번호를 비교해야 합니다
		return inputPassword.equals(storedPassword);
	}

	private UserResponse createLoginResponse(User user, UserStatus userStatus) {
		return new UserResponse(user.getId(), user.getUserid(), user.getUsername(), user.getEmail(),
			userStatus.isOnline(),
			user.getCreatedAt(), user.getUpdatedAt());
	}
}