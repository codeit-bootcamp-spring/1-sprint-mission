package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, Channel channel) {
        if(!data.containsKey(id)) {
            return null;
        }
        data.put(id, channel);
        return channel;
    }

    @Override
    public boolean delete(UUID id) {
        if (!data.containsKey(id)) {
            return false;
        }
        data.remove(id);
        return true;
    }

}
