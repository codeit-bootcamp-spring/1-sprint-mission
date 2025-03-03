package com.sprint.mission.discodeit.user.service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binaryContent.entity.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.entity.UserStatus;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BasicUserService implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final BinaryContentRepository binaryContentRepository;
	private final UserStatusRepository userStatusRepository;

	/**
	 * 새로운 사용자를 생성합니다.
	 * @return 생성된 사용자 정보
	 */
	@Override
	public User createUser(UserCreateRequest userCreateRequest,
		Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
		String username = userCreateRequest.username();
		String email = userCreateRequest.email();

		// 중복 체크
		if (userRepository.existsByUsername(userCreateRequest.username())) {
			throw new IllegalArgumentException("Username already exists: " + userCreateRequest.username());
		}
		if (userRepository.existsByEmail(userCreateRequest.email())) {
			throw new IllegalArgumentException("Email already exists: " + userCreateRequest.email());
		}

		UUID nullableProfileId = optionalProfileCreateRequest
			.map(profileRequest -> {
				String fileName = profileRequest.fileName();
				String contentType = profileRequest.contentType();
				byte[] bytes = profileRequest.bytes();
				BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName, (long)bytes.length);
				return binaryContentRepository.save(binaryContent).getId();
			})
			.orElse(null);

		String password = userCreateRequest.password();

		User user = new User(username, email, password, nullableProfileId);
		User createdUser = userRepository.save(user);

		Instant now = Instant.now();
		UserStatus userStatus = new UserStatus(createdUser.getId(), now);
		userStatusRepository.save(userStatus);

		return user;
	}

	/**
	 * 사용자 ID를 기반으로 사용자를 조회합니다.
	 * @param existUserId 조회할 사용자 ID
	 * @return 조회된 사용자 정보
	 */
	//비밀번호 변경시에는 userresponse에서는 password를 반환하지 않는데 새로운 메서드를 하나 만들어야될까?
	@Override
	public UserResponse findUser(UUID existUserId) {
		log.info("Finding user with ID: {}", existUserId);
		User user = userRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + existUserId));
		log.info("Found user: {}", user);
		Boolean online = userStatusRepository.findByUserId(user.getId())
			.map(UserStatus::isOnline)
			.orElse(null);

		UserResponse response = userMapper.toDto(user);

		return new UserResponse(response.id(), response.createdAt(), response.updatedAt(), response.username(),
			response.email(), response.profileId(), online);
	}

	/**
	 * 모든 사용자를 조회합니다.
	 * @return 전체 사용자 목록
	 */
	@Override
	public List<UserResponse> findAllUsers() {
		List<User> users = userRepository.findAll();

		return users.stream()
			.map(user -> {
				UserResponse response = userMapper.toDto(user);
				Boolean online = userStatusRepository.findByUserId(response.id())
					.map(UserStatus::isOnline)
					.orElse(null);
				return new UserResponse(response.id(), response.createdAt(), response.updatedAt(), response.username(),
					response.email(), response.profileId(), online);
			})
			.collect(Collectors.toList());
	}

	/**
	 * 사용자 정보를 업데이트합니다.
	 * @return 업데이트된 사용자 객체
	 */
	@Override
	public User update(UUID userId, UserUpdateRequest userUpdateRequest,
		Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

		String newUsername = userUpdateRequest.newUsername();
		String newEmail = userUpdateRequest.newEmail();
		if (userRepository.existsByEmail(newEmail)) {
			throw new IllegalArgumentException("User with email " + newEmail + " already exists");
		}
		if (userRepository.existsByUsername(newUsername)) {
			throw new IllegalArgumentException("User with username " + newUsername + " already exists");
		}

		UUID nullableProfileId = optionalProfileCreateRequest
			.map(profileRequest -> {
				Optional.ofNullable(user.getProfileId())
					.ifPresent(binaryContentRepository::deleteById);

				String fileName = profileRequest.fileName();
				String contentType = profileRequest.contentType();
				byte[] bytes = profileRequest.bytes();
				BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName, (long)bytes.length);
				return binaryContentRepository.save(binaryContent).getId();
			})
			.orElse(null);

		String newPassword = userUpdateRequest.newPassword();
		user.update(newUsername, newEmail, newPassword, nullableProfileId);

		return userRepository.save(user);
	}

	/**
	 * 사용자를 삭제합니다.
	 * @param userId 삭제할 사용자 ID
	 */
	@Override
	public void delete(UUID userId) {
		User existUser = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User ID does not exist: " + userId));

		Optional.ofNullable(existUser.getProfileId())
			.ifPresent(binaryContentRepository::deleteById);

		// 관련 도메인 삭제
		userStatusRepository.deleteByUserId(existUser.getId());

		userRepository.deleteById(existUser.getId());
	}

}