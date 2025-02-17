package com.sprint.mission.discodeit.user.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.message.dto.request.binaryContent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.message.entity.BinaryContentType;
import com.sprint.mission.discodeit.message.service.BinaryContentService;
import com.sprint.mission.discodeit.user.dto.request.CreateUserRequest;
import com.sprint.mission.discodeit.user.dto.request.CreateUserStatusRequest;
import com.sprint.mission.discodeit.user.dto.request.UpdateUserRequest;
import com.sprint.mission.discodeit.user.dto.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.entity.UserStatus;
import com.sprint.mission.discodeit.user.repository.UserRepository;

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
	public User createUser(CreateUserRequest request) {
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
		userStatusService.create(new CreateUserStatusRequest(user.getId(), Instant.now()));

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

		return user;
	}

	/**
	 * 사용자 ID를 기반으로 사용자를 조회합니다.
	 * @param existUserId 조회할 사용자 ID
	 * @return 조회된 사용자 정보
	 */
	//비밀번호 변경시에는 userresponse에서는 password를 반환하지 않는데 새로운 메서드를 하나 만들어야될까?
	@Override
	public User findUser(UUID existUserId) {
		User user = userRepository.findById(existUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + existUserId));
		return user;
	}

	/**
	 * 모든 사용자를 조회합니다.
	 * @return 전체 사용자 목록
	 */
	@Override
	public List<User> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users;
	}

	/**
	 * 사용자 정보를 업데이트합니다.
	 * @param existUserId 업데이트할 사용자 ID
	 * @param request 변경할 사용자 정보 (사용자명, 이메일, 프로필 이미지)
	 * @return 업데이트된 사용자 객체
	 */
	@Override
	public User updateUser(UUID existUserId, UpdateUserRequest request) {
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
		return user;
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

}