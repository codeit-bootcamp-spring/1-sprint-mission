package com.sprint.mission.discodeit.channel.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.message.entity.Message;

public interface ChannelService {
	Channel createPrivateChannel(CreatePrivateChannelRequest request);

	void addMessageToChannel(UUID channelId, Message message);

	Channel createPublicChannel(CreatePublicChannelRequest request);

	Channel find(UUID channelId);

	List<Channel> findAllByUserId(UUID userId);

	Channel updateChannel(UUID channelId, UpdateChannelRequest request);

	void addParticipantToChannel(UUID channelId, UUID userId);

	void deleteChannel(UUID channelId);

}
