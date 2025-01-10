package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannel implements ChannelService {
    private final List<Channel> channels = new ArrayList<>();

    @Override
    public Channel createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);
        channels.add(newChannel);
        return newChannel;
    }

    @Override
    public List<Channel> getChannels() {
        return channels;
    }

    @Override
    public Optional<Channel> getChannel(UUID uuid) {
        for (Channel channel : channels) {
            if (channel.getId().equals(uuid)) {
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Channel> addMessageToChannel(UUID uuid, Message message) {
        for (Channel channel : channels) {
            if (channel.getId().equals(uuid)) {
                channel.addMessage(message);
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Channel> updateChannel(UUID uuid, String channelName) {
        for (Channel channel : channels) {
            if (channel.getId().equals(uuid)) {
                channel.updateChannelName(channelName);
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Channel> deleteChannel(UUID uuid) {
        for (Channel channel : channels) {
            if (channel.getId().equals(uuid)) {
                channels.remove(channel);
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }
}
