package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<String, Channel> data = new HashMap<>();

    @Override
    public void create(Channel channel) {
        data.put(channel.getId().toString(), channel);
    }

    @Override
    public Optional<Channel> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(String id, String newName) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.updateName(newName);
        }
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
