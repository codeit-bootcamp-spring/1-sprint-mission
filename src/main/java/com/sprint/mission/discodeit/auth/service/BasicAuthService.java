package com.sprint.mission.discodeit.auth.service;

import java.util.NoSuchElementException;

import com.sprint.mission.discodeit.auth.dto.LoginUserRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.service.UserStatusService;

public class BasicAuthService implements AuthService {
	private final UserRepository userRepository;
	private final UserStatusService userStatusService;

	public BasicAuthService(UserRepository userRepository, UserStatusService userStatusService) {
		this.userRepository = userRepository;
		this.userStatusService = userStatusService;
	}

	@Override
	public User login(LoginUserRequest request) {
		String username = request.username();
		String password = request.password();

		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new NoSuchElementException("User with username " + username + " not found"));

		if (!validatePassword(request.password(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid userid or password");
		}

	/*	//status 온라인으로 바꿔야되는 로직 추가완료
		//프론트에서 Instant하지 말고 그냥 백엔드에서 Instant.now로 대체할까...
		userStatusService.updateByUserId(new UserStatusUpdateRequest(user.getId(), Instant.now()));
*/
		return user;
	}

	private boolean validatePassword(String inputPassword, String storedPassword) {
		// 실제 구현에서는 암호화된 비밀번호를 비교해야 합니다
		return inputPassword.equals(storedPassword);
	}

}