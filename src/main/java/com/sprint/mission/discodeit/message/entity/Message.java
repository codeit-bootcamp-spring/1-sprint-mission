package com.sprint.mission.discodeit.message.entity;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.global.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message extends BaseEntity {
	private String content;
	private UUID authorId;   // User 객체 대신 ID 참조
	private UUID channelId;  // Channel 객체 대신 ID 참조
	private List<UUID> attachmentIds;

	public Message(String content, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
		super();
		this.content = content;
		this.authorId = authorId;
		this.channelId = channelId;
		this.attachmentIds = attachmentIds;
	}

	public void update(String newContent) {
		boolean anyValueUpdated = false;
		if (newContent != null && !newContent.equals(this.content)) {
			this.content = newContent;
			anyValueUpdated = true;
		}

		if (anyValueUpdated) {
			this.updateTime();
		}
	}

	@Override
	public String toString() {
		return "Message{" +
			"id='" + getId() + '\'' +
			"content='" + content + '\'' +
			", author='" + authorId + '\'' +
			", channel='" + channelId + '\'' +
			'}';
	}
}
