package com.sprint.mission.discodeit.message.entity;

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

	public Message(String content, UUID authorId, UUID channelId) {
		super();
		this.content = content;
		this.authorId = authorId;
		this.channelId = channelId;
	}

	public void updateContent(String content) {
		this.content = content;
		updateTime();
	}

	public void updateAuthor(UUID userId) {
		this.authorId = userId;
		updateTime();
	}

	public void updateChannel(UUID channelId) {
		this.channelId = channelId;
		updateTime();
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
