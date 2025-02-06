package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    Map<UUID, Channel> channelMap = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        channelMap.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return channelMap.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        channelMap.remove(channelId);
    }
}
