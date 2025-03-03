package com.sprint.mission.discodeit.channel.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.channel.entity.Channel;

public interface ChannelService {
	Channel createPrivateChannel(PrivateChannelCreateRequest request);

	Channel createPublicChannel(PublicChannelCreateRequest request);

	ChannelResponse find(UUID channelId);

	List<ChannelResponse> findAllByUserId(UUID userId);

	Channel update(UUID channelId, PublicChannelUpdateRequest request);

	void delete(UUID channelId);

}
