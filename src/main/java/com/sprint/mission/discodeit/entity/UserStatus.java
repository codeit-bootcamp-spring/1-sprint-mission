package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private final UUID userId;
	private Instant lastActiveAt;

	public UserStatus(UUID userId, Instant lastActiveAt) {
		this.userId = userId;
		this.lastActiveAt = lastActiveAt;
	}

	// 마지막 활동 시간을 업데이트하는 메서드
	public void updateLastActiveTime(Instant lastActiveAt) {
		this.lastActiveAt = Instant.now();
	}

	// 현재 온라인 여부를 판단하는 메서드
	public boolean isOnline() {
		return lastActiveAt.isAfter(Instant.now().minusSeconds(300)); // 5분 이내 활동이면 온라인
	}
}
