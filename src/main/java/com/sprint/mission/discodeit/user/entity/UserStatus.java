package com.sprint.mission.discodeit.user.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private final UUID userId;
	private Instant lastActiveAt;
	private UserStatusType statusType;

	public UserStatus(UUID userId, Instant lastActiveAt) {
		this.userId = userId;
		this.lastActiveAt = lastActiveAt;
		this.statusType = UserStatusType.ONLINE;
	}

	// 마지막 활동 시간을 업데이트하는 메서드
	public void updateLastActiveTime(Instant lastActiveAt) {
		this.lastActiveAt = lastActiveAt;
		this.statusType = UserStatusType.ONLINE;
	}

	public void setStatusType(UserStatusType statusType) {
		this.statusType = statusType;
	}

	// 현재 온라인 여부를 판단하는 메서드
	public boolean isOnline() {
		if (statusType == UserStatusType.OFFLINE) {
			return false;
		}
		return lastActiveAt.isAfter(Instant.now().minusSeconds(300));
	}
}
