package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> storage = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        storage.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }
}
