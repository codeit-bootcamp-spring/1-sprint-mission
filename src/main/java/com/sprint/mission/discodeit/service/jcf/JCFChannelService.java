package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService() {
        this.data = new ArrayList<>();
    }

    @Override
    public Channel createChannel(UUID id, Long createdAt, Long updatedAt, String name) {
        Channel channel = new Channel(id, createdAt, updatedAt, name);
        data.add(channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        return data.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateChannel(UUID id, String name, Long updatedAt) {
        Channel channel = getChannel(id);
        if (channel != null) {
            channel.update(updatedAt);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        data.removeIf(channel -> channel.getId().equals(id));
    }
}
