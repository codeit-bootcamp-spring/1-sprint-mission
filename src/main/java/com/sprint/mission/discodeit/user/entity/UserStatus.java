package com.sprint.mission.discodeit.user.entity;

import java.time.Duration;
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

	public UserStatus(UUID userId, Instant lastActiveAt) {
		super();
		this.userId = userId;
		this.lastActiveAt = lastActiveAt;
	}

	// 마지막 활동 시간을 업데이트하는 메서드
	public void updateLastActiveTime(Instant lastActiveAt) {
		boolean anyValueUpdated = false;
		if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
			this.lastActiveAt = lastActiveAt;
			anyValueUpdated = true;
		}

		if (anyValueUpdated) {
			this.updateTime();
		}
	}

	// 현재 온라인 여부를 판단하는 메서드
	public boolean isOnline() {
		Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

		return lastActiveAt.isAfter(instantFiveMinutesAgo);
	}
}
