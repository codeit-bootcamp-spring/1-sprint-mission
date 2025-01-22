package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository repository;

    public FileChannelService(ChannelRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createChannel(Channel channel) {
        repository.save(channel);
        System.out.println("[Channel Created]: " + channel);
    }

    @Override
    public Optional<Channel> getChannel(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return repository.findAll();
    }

    @Override
    public void updateChannel(Channel channel) {
        repository.save(channel);
        System.out.println("[Channel Updated]: " + channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        repository.delete(id);
        System.out.println("[Channel Deleted]");
    }
}
