package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(Channel.ChannelType channelType, String channelName, String description) {
        Channel channel = new Channel(channelType, channelName, description);
        return channelRepository.create(channel);
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) throw new NoSuchElementException("Channel not found with ID: " + channelId);
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newChannelDescription) {
        Channel channel = find(channelId);
        channel.updateDescription(newChannelDescription);
        return channelRepository.create(channel);
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }
}
