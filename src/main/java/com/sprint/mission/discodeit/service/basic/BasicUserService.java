package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicUserService implements UserService {

	private final UserRepository userRepository;
	// private final UserStatusRepository userStatusRepository;
	private final UserMapper userMapper;
	private final BinaryContentRepository binaryContentRepository;
	private final BinaryContentStorage binaryContentStorage;

	@Transactional
	@Override
	public UserDto create(UserCreateRequest userCreateRequest,
		Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
		String username = userCreateRequest.username();
		String email = userCreateRequest.email();

		validateIsUserExistsByEmail(email);
		validateIsUserExistsByUsername(username);

		BinaryContent nullableProfile = optionalProfileCreateRequest
			.map(profileRequest -> {
				String fileName = profileRequest.fileName();
				String contentType = profileRequest.contentType();
				byte[] bytes = profileRequest.bytes();
				BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length,
					contentType);
				binaryContentRepository.save(binaryContent);
				binaryContentStorage.put(binaryContent.getId(), bytes);

				log.info("User '{}' profile creation succeed", username);
				return binaryContent;
			})
			.orElse(null);
		String password = userCreateRequest.password();

		User user = new User(username, email, password, nullableProfile);
		Instant now = Instant.now();
		UserStatus userStatus = new UserStatus(user, now);

		userRepository.save(user);

		log.info("{} User creation succeed", user.getId());

		return userMapper.toDto(user);
	}

	private void validateIsUserExistsByUsername(String username) {
		if (userRepository.existsByUsername(username)) {
			String msg = "User with username " + username + " already exists";
			log.warn(msg);
			// throw new IllegalArgumentException(msg);
			throw new UserAlreadyExistsException(msg, null);
		}
	}

	private void validateIsUserExistsByEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			String msg = "User with email " + email + " already exists";
			log.warn(msg);
			throw new UserAlreadyExistsException(msg, null);
			// throw new IllegalArgumentException(msg);
		}
	}

	@Override
	public UserDto find(UUID userId) {
		return userRepository.findById(userId)
			.map(userMapper::toDto)
			.orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
	}

	@Override
	public List<UserDto> findAll() {
		return userRepository.findAllWithProfileAndStatus()
			.stream()
			.map(userMapper::toDto)
			.toList();
	}

	@Transactional
	@Override
	public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
		Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> {
				String msg = "User with id " + userId + " not found";
				log.warn(msg);

				// return new NoSuchElementException(msg);
				return new UserNotFoundException(msg, null);
			});

		String newUsername = userUpdateRequest.newUsername();
		String newEmail = userUpdateRequest.newEmail();

		validateIsUserExistsByEmail(newEmail);
		validateIsUserExistsByUsername(newUsername);

		BinaryContent nullableProfile = optionalProfileCreateRequest
			.map(profileRequest -> {

				String fileName = profileRequest.fileName();
				String contentType = profileRequest.contentType();
				byte[] bytes = profileRequest.bytes();
				BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length,
					contentType);
				binaryContentRepository.save(binaryContent);
				binaryContentStorage.put(binaryContent.getId(), bytes);

				log.info("User '{}' profile update succeed", newUsername);

				return binaryContent;
			})
			.orElse(null);

		String newPassword = userUpdateRequest.newPassword();
		user.update(newUsername, newEmail, newPassword, nullableProfile);

		log.info("{} User update succeed", user.getId());


		return userMapper.toDto(user);
	}

	@Transactional
	@Override
	public void delete(UUID userId) {
		if (!userRepository.existsById(userId)) {
			String msg = "User with id " + userId + " not found";
			log.warn(msg);
			throw new UserNotFoundException(msg, null);
		}

		userRepository.deleteById(userId);

		log.info("{} User deletion succeed", userId);
	}
}
