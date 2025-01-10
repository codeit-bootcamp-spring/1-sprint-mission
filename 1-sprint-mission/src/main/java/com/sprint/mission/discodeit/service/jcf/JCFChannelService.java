package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel read(UUID channelId) {
        return data.get(channelId);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID channelId, String channelName) {
        Channel channel = data.get(channelId);
        if (channel != null) {
            channel.update(channelName);
        }
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }
}
