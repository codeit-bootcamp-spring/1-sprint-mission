package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelList;

    public JCFChannelRepository() {
        this.channelList = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        channelList.put(channel.getChannelId(), channel);
        return channel;
    }

    @Override
    public Map<UUID, Channel> load() {
        return channelList;
    }

    @Override
    public void delete(UUID id) {
        channelList.remove(id);
    }
}
