package com.sprint.mission.discodeit.channel.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.channel.dto.response.channel.ChannelListResponse;
import com.sprint.mission.discodeit.channel.dto.response.channel.ChannelResponse;
import com.sprint.mission.discodeit.channel.entity.Channel;

public interface ChannelService {
	Channel createPrivateChannel(CreatePrivateChannelRequest request);

	Channel createPublicChannel(CreatePublicChannelRequest request);

	ChannelResponse find(UUID channelId);

	List<ChannelListResponse> findAllByUserId(UUID userId);

	Channel updateChannel(UUID channelId, UpdateChannelRequest request);

	void addParticipantToChannel(UUID channelId, UUID userId);

	void deleteChannel(UUID channelId);
}
