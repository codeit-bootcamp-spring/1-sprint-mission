package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(Channel channel) {
        if (channel.getId() == null) {
            channel.setId(UUID.randomUUID());
        }
        return channelRepository.save(channel);
    }

    @Override
    public Channel findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return channelRepository.findById(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(String id, Channel updated) {
        try {
            UUID uuid = UUID.fromString(id);
            Channel existing = channelRepository.findById(uuid);
            if (existing == null) {
                throw new RuntimeException("Channel not found for ID: " + id);
            }
            existing.update(updated.getName());
            channelRepository.save(existing);
            return existing;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            channelRepository.delete(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }
}
