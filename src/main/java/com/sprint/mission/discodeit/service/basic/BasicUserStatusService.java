package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.userStatus.request.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

public class BasicUserStatusService implements UserStatusService {
	private final UserStatusRepository userStatusRepository;
	private final UserRepository userRepository;

	public BasicUserStatusService(UserStatusRepository userStatusRepository, UserRepository userRepository) {
		this.userStatusRepository = userStatusRepository;
		this.userRepository = userRepository;
	}

	/**
	 * 새로운 사용자 상태를 생성합니다.
	 * @param request 사용자 상태 생성 요청 정보 (사용자 ID, 마지막 활동 시간)
	 * @return 생성된 사용자 상태
	 */
	@Override
	public UserStatus create(CreateUserStatusRequest request) {
		// 사용자 존재 여부 확인
		userRepository.findById(request.userId())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// 사용자 상태 중복 체크
		if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
			throw new IllegalArgumentException("UserStatus already exists");
		}

		// 새로운 사용자 상태 생성 및 저장
		UserStatus userStatus = new UserStatus(request.userId(), request.lastActiveAt());
		return userStatusRepository.save(userStatus);
	}

	/**
	 * ID로 사용자 상태를 조회합니다.
	 * @param id 사용자 상태 ID
	 * @return 사용자 상태
	 */
	@Override
	public UserStatus find(UUID id) {
		return userStatusRepository.findByUserId(id)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
	}

	/**
	 * 온라인 상태인 모든 사용자의 상태를 조회합니다.
	 * @return 온라인 사용자 상태 목록
	 */
	@Override
	public List<UserStatus> findAll() {
		return userStatusRepository.findAllOnlineUsers();
	}

	/**
	 * 사용자 상태를 업데이트합니다.
	 * @param id 사용자 상태 ID
	 * @param request 상태 업데이트 요청 정보
	 * @return 업데이트된 사용자 상태
	 */
	@Override
	public UserStatus update(UUID id, UpdateUserStatusRequest request) {
		// 사용자 상태 조회 및 업데이트
		UserStatus userStatus = userStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

		userStatus.updateLastActiveTime(request.lastActiveAt());
		return userStatusRepository.save(userStatus);
	}

	/**
	 * 사용자 ID로 상태를 업데이트합니다.
	 * @param userId 사용자 ID
	 * @param lastActiveAt 상태 업데이트 요청 정보
	 * @return 업데이트된 사용자 상태
	 */
	//createUserRequest dto와 받는 파라미터들은 같은데 그냥 createUserRequest를 사용하는게 맞을까? 원래 dto UpdateUserStatusRequest
	@Override
	public UserStatus updateByUserId(UUID userId, Instant lastActiveAt) {
		// 사용자 상태 조회 및 업데이트
		UserStatus userStatus = userStatusRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

		// 백엔드에서 시간을 now로 update하는것이 아닌 프론트 기준으로 보낸 시간을 update하는 걸로 수정
		userStatus.updateLastActiveTime(lastActiveAt);
		return userStatusRepository.save(userStatus);
	}

	/**
	 * 사용자 상태를 삭제합니다.
	 * @param id 삭제할 사용자 상태 ID
	 */
	@Override
	public void deleteByUserId(UUID id) {
		//사용자가 있는지 확인
		//사용자 상태 존재 여부 확인 후 사용자 상태만 삭제하는 부분도 추가로 구현해야될까...?
		userStatusRepository.findByUserId(id)
			.orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
		userStatusRepository.deleteByUserId(id);
	}
}
