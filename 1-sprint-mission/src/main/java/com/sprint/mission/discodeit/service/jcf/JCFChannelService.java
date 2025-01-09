package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<String, Channel> data = new HashMap<>();

    @Override
    public Channel create(Channel channel) {
        data.put(channel.getId().toString(), channel);
        return channel;
    }

    @Override
    public Channel findById(String id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(String id, Channel channel) {
        if(data.containsKey(id)) {
            data.put(id, channel);
            return channel;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
