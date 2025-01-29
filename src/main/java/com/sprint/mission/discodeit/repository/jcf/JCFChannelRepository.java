package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new ConcurrentHashMap<>();

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean update(UUID id, String name, String topic, ChannelType type) {
        return data.computeIfPresent(id, (key, c) -> {
            c.update(name, topic, type);
            return c;
        }) != null;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}
