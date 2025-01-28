package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final List<Channel> channels = new ArrayList<>();

    @Override
    public void save(Channel channel) {
        channels.add(channel);
    }

    @Override
    public Channel findByChannelname(String channelname) {
        for (Channel channel : channels) {
            if (channel.getChannelName().equals(channelname)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public Channel findById(UUID id) {
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return channels;
    }

    @Override
    public void deleteById(UUID id) {
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(id)) {
                channels.remove(i);
                return;
            }
        }
    }
}