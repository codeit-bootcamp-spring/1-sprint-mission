package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Message extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
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
