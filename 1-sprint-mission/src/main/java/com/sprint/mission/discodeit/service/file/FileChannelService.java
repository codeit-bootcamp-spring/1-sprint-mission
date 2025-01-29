package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String name) {
        Channel channel = new Channel(name);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String newName) {
        Channel existing = channelRepository.findById(id);
        if (existing != null) {
            existing.update(newName);
            return channelRepository.save(existing);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}
