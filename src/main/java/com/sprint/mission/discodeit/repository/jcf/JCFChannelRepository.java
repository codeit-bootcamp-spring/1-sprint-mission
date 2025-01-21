package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelData;

    public JCFChannelRepository(Map<UUID, Channel> channelData) {
        this.channelData = channelData;
    }

    @Override
    public Channel save(Channel channel) {
        channelData.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelData.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public void delete(UUID id) {
        channelData.remove(id);
    }
}
