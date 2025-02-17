package com.sprint.mission.discodeit.channel.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;

@Getter
public class ReadStatus extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	//특정 사용자가
	private final UUID userId;
	//특정 채널에서
	private final UUID channelId;
	//마지막으로 어떤 메시지를 읽었는지
	private UUID lastCheckedMessageId;
	// 마지막으로 읽은 시간(messageid가 들어가면 lastReadat이 필요로 한가? || 사용자가 실제 읽음을 반영한 시간)
	private Instant lastReadAt;

	public ReadStatus(UUID userId, UUID channelId, UUID lastCheckedMessageId, Instant lastReadAt) {
		this.userId = userId;
		this.channelId = channelId;
		this.lastCheckedMessageId = lastCheckedMessageId;
		this.lastReadAt = lastReadAt;
	}

	public void updateLastReadAt(UUID messageId, Instant lastReadAt) {
		this.lastCheckedMessageId = messageId;
		this.lastReadAt = lastReadAt;
		updateTime();
	}
}
