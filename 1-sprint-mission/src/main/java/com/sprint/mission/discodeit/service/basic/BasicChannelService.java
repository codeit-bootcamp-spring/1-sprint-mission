package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(Channel channel) {
        channelRepository.save(channel);
        System.out.println("Channel created: " + channel.getChannelTitle() + " (UUID: " + channel.getChannelUuid() + ")");
    }

    @Override
    public Channel readChannel(String channelUuid) {
        return channelRepository.findByUuid(channelUuid)
                .orElseThrow(()-> new NoSuchElementException("Channel not found: " + channelUuid));
    }

    @Override
    public List<Channel> readAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(String channelUuid, String newChannelTitle) {
        Channel channel = channelRepository.findByUuid(channelUuid)
                .orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelUuid));
        channel.setChannelTitle(newChannelTitle);
    }

    @Override
    public void deleteChannel(String channelUuid) {
        channelRepository.delete(channelUuid);
        System.out.println("Channel deleted: UUID " + channelUuid);
    }
}
