package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.collection.Channels;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannel implements ChannelService {
    private final Channels channels = new Channels();

    @Override
    public Channel createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);
        channels.add(newChannel);
        return newChannel;
    }

    @Override
    public Optional<Channel> getChannel(UUID uuid) {
        return channels.get(uuid);
    }

    @Override
    public Optional<Channel> addMessageToChannel(UUID channelUUID, UUID messageUUID) {
        return channels.addMessageToChannel(channelUUID, messageUUID);
    }

    @Override
    public Optional<Channel> updateChannel(UUID uuid, String channelName) {
        return channels.updateName(uuid, channelName);
    }

    @Override
    public Optional<Channel> deleteChannel(UUID uuid) {
        return channels.remove(uuid);
    }

    @Override
    public Map<UUID, Channel> getChannels() {
        return channels.asReadOnly();
    }
}
