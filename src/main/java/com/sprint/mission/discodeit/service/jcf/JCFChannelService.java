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
    public void createChannel(Channel channel) {
        data.add(channel);
    }

    @Override
    public Optional<Channel> readChannel(UUID id) {
        return data.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> readAllChannels() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateChannel(UUID id, String name, String topic) {
        readChannel(id).ifPresent(channel -> channel.update(name, topic));
    }

    @Override
    public void deleteChannel(UUID id) {
        data.removeIf(channel -> channel.getId().equals(id));
    }
}