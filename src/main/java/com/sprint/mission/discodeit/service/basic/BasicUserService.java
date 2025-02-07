package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
	private final UserRepository userRepository;
	//1. 이 둘은 나중에 repository가 아닌 service로 불러와야되지 않을까..?
	//2. constructor가 아닌 setter주입으로 혹시 모를 순환참조를 막는게 좋지 않을까...?
	private final UserStatusRepository userStatusRepository;
	private final BinaryContentRepository binaryContentRepository;

	@Override
	public UserResponse createUser(CreateUserRequest requestDto) {
		// 중복 체크
		if (userRepository.findByUsername(requestDto.username()).isPresent()) {
			throw new IllegalArgumentException("Username already exists: " + requestDto.username());
		}
		if (userRepository.findByEmail(requestDto.email()).isPresent()) {
			throw new IllegalArgumentException("Email already exists: " + requestDto.email());
		}
		//user 생성
		User user = new User(requestDto.userid(), requestDto.username(), requestDto.email(), requestDto.password());
		user = userRepository.save(user);

		//userstatus 생성
		UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
		userStatusRepository.save(userStatus);

		//프로필 이미지가 있다면 저장
		if (requestDto.profileImage() != null) {
			BinaryContent profileImage = new BinaryContent(
				UUID.randomUUID(),
				Instant.now(),
				user.getId(),
				null,
				requestDto.profileImage(),
				requestDto.contentType(),
				requestDto.fileName(),
				requestDto.profileImage().length
			);
			binaryContentRepository.save(profileImage);
		}

		return createUserResponse(user, userStatus);
	}

	@Override
	public UserResponse findUser(UUID existUserId) {
		User user = userRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + existUserId));
		UserStatus userStatus = userStatusRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found: " + existUserId));

		return createUserResponse(user, userStatus);
	}

	@Override
	public List<UserResponse> findAllUsers() {
		return userRepository.findAll().stream()
			.map(user -> {
				UserStatus status = userStatusRepository.findByUserId(user.getId())
					.orElseThrow();
				return createUserResponse(user, status);
			})
			.collect(Collectors.toList());
	}

	@Override
	public User updateUser(UUID existUserId, UpdateUserRequest requestDto) {
		//isPresent를 통해 orElseThrow를 만들려고 했는데 잘 됐는지 잘 모르겠다.
		User user = userRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// 프로필 이미지 업데이트
		if (requestDto.profileImage() != null) {
			binaryContentRepository.findByAuthorId(existUserId)
				.forEach(content -> binaryContentRepository.deleteById(content.getId()));

			BinaryContent newProfile = new BinaryContent(
				UUID.randomUUID(),
				Instant.now(),
				existUserId,
				null,
				requestDto.profileImage(),
				requestDto.contentType(),
				requestDto.fileName(),
				requestDto.profileImage().length
			);
			binaryContentRepository.save(newProfile);
		}

		user.updateUserid(requestDto.userid());
		user.updateUsername(requestDto.username());
		user.updateUserEmail(requestDto.email());

		return userRepository.save(user);
	}

	@Override
	public void deleteUser(UUID userId) {
		User existUser = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User ID does not exist: " + userId));

		// 관련 도메인 삭제
		userStatusRepository.deleteByUserId(existUser.getId());
		binaryContentRepository.findByAuthorId(existUser.getId())
			.forEach(content -> binaryContentRepository.deleteById(content.getId()));

		userRepository.delete(existUser.getId());
	}

	//메서드로 만들어 return
	private UserResponse createUserResponse(User user, UserStatus userStatus) {
		return new UserResponse(
			user.getUserid(),
			user.getUsername(),
			user.getEmail(),
			userStatus.isOnline(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);
	}
}
