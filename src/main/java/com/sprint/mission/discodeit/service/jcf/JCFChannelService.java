package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
        System.out.println("[Channel Created]: " + channel);
    }

    @Override
    public Optional<Channel> getChannel(UUID id) {
        Optional<Channel> channel = Optional.ofNullable(data.get(id));
        System.out.println("[Channel Retrieved]: " + channel.orElse(null));
        return channel;
    }

    @Override
    public List<Channel> getAllChannels() {
        List<Channel> channels = new ArrayList<>(data.values());
        System.out.println("[All Channels Retrieved]: " + channels);
        return channels;
    }

    @Override
    public void updateChannel(Channel channel) {
        if (data.containsKey(channel.getId())) {
            data.put(channel.getId(), channel);
            System.out.println("[Channel Updated]: " + channel);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        Channel removedChannel = data.remove(id);
        System.out.println("[Channel Deleted]: " + removedChannel);
    }
}