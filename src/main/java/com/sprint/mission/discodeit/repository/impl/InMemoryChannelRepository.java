package com.sprint.mission.discodeit.repository.impl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryChannelRepository implements ChannelRepository {
    List<Channel> data = new ArrayList<>();

    @Override
    public Optional<Channel> findChannelById(UUID id) {
        return data.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst();
    }

    @Override
    public Optional<Channel> findChannelByName(String name) {
        return data.stream()
            .filter(c -> c.getName().equals(name))
            .findFirst();
    }

    @Override
    public List<Channel> findAllChannels() {
        return data;
    }

    @Override
    public void save(Channel channel) {
        data.add(channel);
    }

    @Override
    public void remove(UUID id) {
        findChannelById(id).ifPresent(data::remove);
    }
}
