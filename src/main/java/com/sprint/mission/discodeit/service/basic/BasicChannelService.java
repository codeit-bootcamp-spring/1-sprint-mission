package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository repository;

    public BasicChannelService(ChannelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Channel createChannel(String name, String description) {
        Channel channel = new Channel(name, description);
        repository.save(channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return repository.findAll();
    }

    @Override
    public Channel updateChannel(UUID id, String name, String description) {
        Channel channel = repository.findById(id);
        if(channel != null) {
            channel.update(name, description);
            repository.save(channel);
            return channel;
        }
        return null;
    }

    @Override
    public void deleteChannel(UUID id) {
        repository.delete(id);
    }
}

