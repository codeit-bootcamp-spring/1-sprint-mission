package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        this.data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        boolean exists = this.existsById(id);
        if (!exists) {
            throw new IllegalArgumentException("Channel not found");
        }
        return Optional.ofNullable(this.data.get(id));    }

    @Override
    public List<Channel> findAll() {
        List<Channel> channels = this.data.values().stream().toList();
        if(channels.isEmpty()) {
            System.out.println("No channels found");
            return Collections.emptyList();
        }
        return channels;
    }



    @Override
    public boolean existsById(UUID id) {

        return this.data.containsKey(id);
    }


    @Override
    public void deleteById(UUID id) {
        boolean exists = this.existsById(id);
        if(!exists) {
            throw new IllegalArgumentException("Channel not found");
        }
        System.out.println("Channel deleted: " + id);
        this.data.remove(id);
    }

}
