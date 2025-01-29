package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JcfChannelRepository implements ChannelRepository {
    private final Map<UUID,Channel> data=new HashMap<>();

    @Override
    public void createChannel(UUID id, Channel channel) {
        data.put(channel.getId(),channel);
    }

    @Override
    public void updateChannelName(UUID id, String name) {
        data.get(id).updateChannelName(name);
    }

    @Override
    public void updateDescript(UUID id, String descript) {
        data.get(id).updateDescription(descript);
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }
}
