package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

public class BasicUserService implements UserService {
	private final UserRepository userRepository;
	private final UserStatusService userStatusService;
	private final BinaryContentService binaryContentService;

	public BasicUserService(UserRepository userRepository, UserStatusService userStatusService,
		BinaryContentService binaryContentService) {
		this.userRepository = userRepository;
		this.userStatusService = userStatusService;
		this.binaryContentService = binaryContentService;
	}

	/**
	 * 새로운 사용자를 생성합니다.
	 * @param request 사용자 생성 요청 정보 (ID, 사용자명, 이메일, 비밀번호, 프로필 이미지)
	 * @return 생성된 사용자 정보
	 */
	@Override
	public UserResponse createUser(CreateUserRequest request) {
		// 중복 체크
		if (userRepository.findByUsername(request.username()).isPresent()) {
			throw new IllegalArgumentException("Username already exists: " + request.username());
		}
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalArgumentException("Email already exists: " + request.email());
		}

		// user 생성
		User user = new User(request.userid(), request.username(), request.email(), request.password());
		user = userRepository.save(user);

		// userstatus 생성
		UserStatus userStatus = userStatusService.create(new CreateUserStatusRequest(user.getId(), Instant.now()));

		// 프로필 이미지가 있다면 저장
		if (request.profileImage() != null) {
			try {
				CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(user.getId(), null,
					request.profileImage(), BinaryContentType.PROFILE_IMAGE);
				binaryContentService.create(binaryRequest);
			} catch (Exception e) {
				throw new RuntimeException("Failed to process profile image", e);
			}
		}

		return createUserResponse(user, userStatus);
	}

	/**
	 * 사용자 ID를 기반으로 사용자를 조회합니다.
	 * @param existUserId 조회할 사용자 ID
	 * @return 조회된 사용자 정보
	 */
	//비밀번호 변경시에는 userresponse에서는 password를 반환하지 않는데 새로운 메서드를 하나 만들어야될까?
	@Override
	public UserResponse findUser(UUID existUserId) {
		User user = userRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + existUserId));
		UserStatus userStatus = userStatusService.find(existUserId);

		return createUserResponse(user, userStatus);
	}

	/**
	 * 사용자 ID를 기반으로 User 도메인 객체를 반환합니다.
	 * @param existUserId 조회할 사용자 ID
	 * @return 조회된 사용자 객체
	 */
	@Override
	public User findUserEntity(UUID existUserId) {
		return userRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + existUserId));
	}

	/**
	 * 모든 사용자를 조회합니다.
	 * @return 전체 사용자 목록
	 */
	@Override
	public List<UserResponse> findAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserResponse> responses = new ArrayList<>();

		for (User user : users) {
			UserStatus status = userStatusService.find(user.getId());
			responses.add(createUserResponse(user, status));
		}

		return responses;
	}

	/**
	 * 사용자 정보를 업데이트합니다.
	 * @param existUserId 업데이트할 사용자 ID
	 * @param request 변경할 사용자 정보 (사용자명, 이메일, 프로필 이미지)
	 * @return 업데이트된 사용자 객체
	 */
	@Override
	public UserResponse updateUser(UUID existUserId, UpdateUserRequest request) {
		User user = userRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
		UserStatus userStatus = userStatusService.updateByUserId(
			new UpdateUserStatusRequest(user.getId(), request.requestAt()));
		// 프로필 이미지 업데이트
		if (request.profileImage() != null) {
			try {
				// 기존 프로필 이미지 삭제
				binaryContentService.deleteProfileImageByAuthorId(existUserId);

				// 새로운 프로필 이미지 저장
				CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(user.getId(),
					null, request.profileImage(), BinaryContentType.PROFILE_IMAGE);
				binaryContentService.create(binaryRequest);
			} catch (Exception e) {
				throw new RuntimeException("Failed to process profile image", e);
			}
		}

		user.updateUserid(request.userid());
		user.updateUsername(request.username());
		user.updateUserEmail(request.email());
		userRepository.save(user);
		return createUserResponse(user, userStatus);
	}

	/**
	 * 사용자를 삭제합니다.
	 * @param userId 삭제할 사용자 ID
	 */
	@Override
	public void deleteUser(UUID userId) {
		User existUser = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User ID does not exist: " + userId));

		// 관련 도메인 삭제
		userStatusService.deleteByUserId(userId);
		binaryContentService.delete(userId);

		userRepository.delete(existUser.getId());
	}

	/**
	 * 사용자와 사용자 상태를 기반으로 사용자 응답 객체를 생성합니다.
	 * @param user 사용자 객체
	 * @param userStatus 사용자 상태 객체
	 * @return 사용자 응답 객체
	 */
	private UserResponse createUserResponse(User user, UserStatus userStatus) {
		return new UserResponse(
			user.getId(),
			user.getUserid(),
			user.getUsername(),
			user.getEmail(),
			userStatus.isOnline(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);
	}
}