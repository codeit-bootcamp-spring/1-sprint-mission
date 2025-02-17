package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;

import com.sprint.mission.discodeit.dto.user.request.LoginUserRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;

public class BasicAuthService implements AuthService {
	private final UserRepository userRepository;
	private final UserStatusService userStatusService;

	public BasicAuthService(UserRepository userRepository, UserStatusService userStatusService) {
		this.userRepository = userRepository;
		this.userStatusService = userStatusService;
	}

	@Override
	public UserResponse login(LoginUserRequest request) {
		User user = userRepository.findByUserid(request.userId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid userid or password"));
		if (!validatePassword(request.password(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid userid or password");
		}
		UserStatus userStatus = userStatusService.find(user.getId());

		//status 온라인으로 바꿔야되는 로직 추가완료
		//프론트에서 Instant하지 말고 그냥 백엔드에서 Instant.now로 대체할까...
		userStatusService.updateByUserId(user.getId(), Instant.now());

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