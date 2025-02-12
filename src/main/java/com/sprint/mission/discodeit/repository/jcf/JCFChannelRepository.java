package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

public class JCFChannelRepository implements ChannelRepository {
	private final Map<UUID, Channel> channelData = new HashMap<>();

	@Override
	public Channel save(Channel channel) {
		channelData.put(channel.getId(), channel);
		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID id) {
		return Optional.ofNullable(channelData.get(id));
	}

	@Override
	public List<Channel> findAll() {
		return new ArrayList<>(channelData.values());
	}

	@Override
	public void delete(UUID id) {
		channelData.remove(id);
	}

	@Override
	public List<Channel> findAllPublicChannels() {
		List<Channel> publicChannels = new ArrayList<>();
		for (Channel channel : channelData.values()) {
			if (channel.getChannelType() == ChannelType.PUBLIC) {
				publicChannels.add(channel);
			}
		}
		return publicChannels;
	}

	@Override
	public List<Channel> findPrivateChannelsByUserId(UUID userId) {
		List<Channel> privateChannels = new ArrayList<>();
		for (Channel channel : channelData.values()) {
			if (channel.getChannelType() == ChannelType.PRIVATE) {
				Map<UUID, UserResponse> participants = channel.getParticipants();
				if (participants != null && participants.containsKey(userId)) {
					privateChannels.add(channel);
				}
			}
		}
		return privateChannels;
	}
}
