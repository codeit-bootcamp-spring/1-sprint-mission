package com.sprint.mission.discodeit.user.entity;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatus extends BaseEntity {
	private UUID userId;
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
