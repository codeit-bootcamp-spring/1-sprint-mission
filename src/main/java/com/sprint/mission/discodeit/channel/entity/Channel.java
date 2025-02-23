package com.sprint.mission.discodeit.channel.entity;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Channel extends BaseEntity {
	private String name;
	private String description;
	private Map<UUID, User> participants;
	private Instant lastMessageAt;
	//messagelist는 hashmap은 순서 보장을 안하니 list, linkedHashMap중에 고민
	private List<Message> messageList;
	private ChannelType channelType;

	public Channel(String name, String description, Map<UUID, User> participants, Instant lastMessageAt,
		List<Message> messageList, ChannelType channelType) {
		super();
		this.name = name;
		this.description = description;
		this.participants = participants;
		this.lastMessageAt = Instant.now();
		this.messageList = messageList;
		this.channelType = channelType;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	public void addParticipant(UUID userId, User user) {
		if (participants.containsKey(userId)) {
			throw new IllegalArgumentException("User already in channel: " + userId);
		}
		participants.put(userId, user);
	}

	public void updateParticipants(Map<UUID, User> participants) {
		this.participants = participants;
	}

	public void updateLastMessageAt(Instant newLastMessageAt) {
		this.lastMessageAt = newLastMessageAt;
	}

	public void updateMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public void updateChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}

	@Override
	public String toString() {
		return "Channel{" +
			"name='" + name + '\'' +
			", description='" + description + '\'' +
			", participants=[" + participants.values() + "]" +
			", messageCount=" + ((messageList != null) ? messageList.size() : 0) +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Channel channel = (Channel)o;
		return getId().equals(channel.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
