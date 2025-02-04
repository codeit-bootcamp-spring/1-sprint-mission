package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void create(Channel channel) {
        channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public void update(UUID id, Channel channel) {
        if (channelRepository.findById(id).isPresent()) {
            channelRepository.save(channel);
        } else {
            throw new IllegalArgumentException("Channel not found for ID: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}
