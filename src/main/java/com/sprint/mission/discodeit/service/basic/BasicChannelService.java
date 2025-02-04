package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository repository;
    private static volatile BasicChannelService instance;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.repository = channelRepository;
    }
    public static BasicChannelService getInstance(ChannelRepository channelRepository) {
        if (instance == null) {
            synchronized (BasicChannelService.class) {
                if (instance == null) {
                    instance = new BasicChannelService(channelRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public UUID createChannel(String userName) {
        return repository.save(userName);
    }

    @Override
    public Channel getChannel(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Channel> getChannels() {
        return repository.findAll();
    }

    @Override
    public void updateChannel(UUID id, String name) {
        repository.update(id, name);
    }

    @Override
    public void deleteChannel(UUID id) {
        repository.delete(id);
    }
}
