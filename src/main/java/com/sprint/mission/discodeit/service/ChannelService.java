package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelResponse createChannel(CreateChannelRequest request);

  List<ChannelResponse> getChannelsResponse();

  ChannelResponse getChannelResponse(UUID uuid);

  Channel getChannel(UUID uuid);

  List<Message> getMessagesFromChannel(UUID uuid);

  ChannelResponse addMessageToChannel(UUID channelUUID, Message message);

  ChannelResponse updateChannel(UUID uuid, UpdateChannelRequest request);

  void deleteChannel(UUID uuid);

  ChannelResponse createPrivateChannel(CreatePrivateChannelRequest request);

  List<ChannelResponse> getChannelsByUserId(UUID userId);
}
