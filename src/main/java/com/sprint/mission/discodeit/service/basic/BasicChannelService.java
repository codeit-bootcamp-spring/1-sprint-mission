package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private static volatile BasicChannelService instance;
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    protected static BasicChannelService getInstance(ChannelRepository channelRepository) {
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
    public Channel createChannel(String name, String topic, ChannelType type) {
        return channelRepository.save(new Channel(name, topic, type));
    }

    @Override
    public Optional<Channel> getChannelDetails(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public boolean editChannel(UUID id, String name, String topic, ChannelType type) {
        Optional<Channel> byId = channelRepository.findById(id);
        if (byId.isPresent()) {
            Channel channel = byId.get();
            channel.update(name, topic, type);
            channelRepository.save(channel);
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteChannel(UUID id) {
        return channelRepository.delete(id);
    }
}
