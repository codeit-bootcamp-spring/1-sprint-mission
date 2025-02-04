package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final HashMap<UUID, Channel> data = new HashMap<>();

    @Override
    public void save(Channel channel) {
        data.put(channel.getChanneluuId(), channel);
    }

    @Override
    public Channel findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public HashMap<UUID, Channel> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
    }
}
