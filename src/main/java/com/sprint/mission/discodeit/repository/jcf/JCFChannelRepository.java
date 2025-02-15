package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels;

    public JCFChannelRepository() {
        this.channels = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return channels.values().stream().toList();
    }

    @Override
    public void update(Channel channel, String name, String introduction) {
        channel.update(name, introduction);
    }

    @Override
    public void delete(Channel channel) {
        channels.remove(channel.getId());
    }
}
