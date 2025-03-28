package com.sprint.mission.discodeit.service.basic;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicAuthService implements AuthService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Transactional(readOnly = true)
	@Override
	public UserDto login(LoginRequest loginRequest) {
		String username = loginRequest.username();
		String password = loginRequest.password();
		log.debug("로그인 처리 시작 - username: {}", loginRequest.username());
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				log.warn("로그인 실패 - 존재하지 않는 사용자: {}", username);
				return new NoSuchElementException("User with username " + username + "not found");
			});

		if (!user.getPassword().equals(password)) {
			log.warn("로그인 실패 - 잘못된 비밀번호: {}", username);
			throw new IllegalArgumentException("Wrong password");
		}

		log.info("로그인 성공 - username: {}", username);
		return userMapper.toDto(user);
	}
}
