package com.sprint.mission.discodeit.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.user.entity.UserStatus;

public interface UserStatusRepository {
	UserStatus save(UserStatus userStatus);

	Optional<UserStatus> findById(UUID id);

	//사용자의 현재 접속 상태 조회
	Optional<UserStatus> findByUserId(UUID userId);

	List<UserStatus> findAll();
	
	//현재 접속 중인 모든 사용자 목록 조회
	List<UserStatus> findAllOnlineUsers();

	void deleteByUserId(UUID userId);
}
