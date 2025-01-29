package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public FileChannelService(File filePath) {
        this.channelRepository = new FileChannelRepository(filePath);
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
