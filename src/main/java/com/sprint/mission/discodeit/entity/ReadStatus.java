package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class ReadStatus extends BaseEntity {
	private final UUID userId;
	private final UUID channelId;
	private Instant lastReadAt;

	public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
		this.userId = userId;
		this.channelId = channelId;
		this.lastReadAt = lastReadAt;
	}

	public void updateLastReadAt(Instant lastReadAt) {
		this.lastReadAt = lastReadAt;
	}
}
