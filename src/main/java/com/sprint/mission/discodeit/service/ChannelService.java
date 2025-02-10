package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;

public interface ChannelService {
	Channel createPrivateChannel(CreatePrivateChannelRequest request);

	Channel createPublicChannel(CreatePublicChannelRequest request);

	ChannelResponse find(UUID channelId);

	List<ChannelResponse> findAll();

	Channel updateChannel(UUID channelId, Channel updateChannel);

	boolean deleteChannel(UUID channelId);

	void addParticipantToChannel(UUID channelId, UUID userId);
}
