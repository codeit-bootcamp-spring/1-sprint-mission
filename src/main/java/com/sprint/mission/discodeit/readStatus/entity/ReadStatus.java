package com.sprint.mission.discodeit.readStatus.entity;

import java.time.Instant;
import java.util.UUID;

import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadStatus extends BaseEntity {
	//특정 사용자가
	private UUID userId;
	//특정 채널에서
	private UUID channelId;
	// 마지막으로 읽은 시간(messageid가 들어가면 lastReadat이 필요로 한가? || 사용자가 실제 읽음을 반영한 시간)
	private Instant lastReadAt;

	public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
		super();
		this.userId = userId;
		this.channelId = channelId;
		this.lastReadAt = lastReadAt;
	}

	public void update(Instant newLastReadAt) {
		boolean anyValueUpdated = false;
		if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
			this.lastReadAt = newLastReadAt;
			anyValueUpdated = true;
		}

		if (anyValueUpdated) {
			this.updateTime();
		}
	}
}
