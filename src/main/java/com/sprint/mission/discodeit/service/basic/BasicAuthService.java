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
		log.debug("Processing login - username: {}", loginRequest.username());

		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> {
				log.warn("Login failed - User not found: {}", username);
				return new NoSuchElementException("User with username " + username + " not found");
			});

		if (!user.getPassword().equals(password)) {
			log.warn("Login failed - Incorrect password: {}", username);
			throw new IllegalArgumentException("Wrong password");
		}

		log.info("Login successful - username: {}", username);
		return userMapper.toDto(user);
	}
}