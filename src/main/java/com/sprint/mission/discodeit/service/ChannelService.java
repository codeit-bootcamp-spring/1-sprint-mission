package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    ChannelResponse createChannel(CreateChannelRequest request);

    List<ChannelResponse> getChannels();

    Optional<ChannelResponse> getChannel(UUID uuid);

    List<UUID> getMessagesUUIDFromChannel(UUID uuid);

    Optional<ChannelResponse> addMessageToChannel(UUID channelUUID, UUID messageUUID);

    Optional<ChannelResponse> updateChannel(UUID uuid, String channelName);

    void deleteChannel(UUID uuid);
}
