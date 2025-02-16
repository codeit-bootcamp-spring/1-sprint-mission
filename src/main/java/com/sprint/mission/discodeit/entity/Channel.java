package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.user.response.UserResponse;

import lombok.Getter;

@Getter
public class Channel extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private Map<UUID, UserResponse> participants;
	//messagelist는 hashmap은 순서 보장을 안하니 list, linkedHashMap중에 고민
	private List<Message> messageList;
	private ChannelType channelType;

	public Channel(String name, String description, Map<UUID, UserResponse> participants, List<Message> messageList,
		ChannelType channelType) {
		super();
		this.name = name;
		this.description = description;
		this.participants = participants;
		this.messageList = messageList;
		this.channelType = channelType;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	public void addParticipant(UUID userId, UserResponse userResponse) {
		if (participants.containsKey(userId)) {
			throw new IllegalArgumentException("User already in channel: " + userId);
		}
		participants.put(userId, userResponse);
	}

	public void updateParticipants(Map<UUID, UserResponse> participants) {
		this.participants = participants;
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
