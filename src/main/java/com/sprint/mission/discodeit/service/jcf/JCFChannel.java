package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannel implements ChannelService {
    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public Channel createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);
        channels.put(newChannel.getId(), newChannel);
        return newChannel;
    }

    @Override
    public Map<UUID, Channel> getChannels() {
        return channels;
    }

    @Override
    public Optional<Channel> getChannel(UUID uuid) {
        return Optional.ofNullable(channels.get(uuid));
    }

    @Override
    public Optional<Channel> addMessageToChannel(UUID channelUUID, UUID messageUUID) {
        Channel channel = channels.get(channelUUID);
        if (channel != null) {
            channel.addMessageToChannel(messageUUID);
            return Optional.of(channel);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Channel> updateChannel(UUID uuid, String channelName) {
        Channel channel = channels.get(uuid);
        if (channel != null) {
            channel.updateChannelName(channelName);
            return Optional.of(channel);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Channel> deleteChannel(UUID uuid) {
        Channel channel = channels.get(uuid);
        if (channel != null) {
            channels.remove(channel);
            return Optional.of(channel);
        }
        return Optional.empty();
    }
}
