package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;

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
        return channelRepository.findByUuid(channelUuid);
    }

    @Override
    public List<Channel> readAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(String channelUuid, String newChannelTitle) {
        channelRepository.delete(channelUuid);
        channelRepository.save(channelRepository.findByUuid(channelUuid));
        System.out.println("Channel updated: " + channelRepository.findByUuid(channelUuid).getChannelTitle() + " (UUID: " + channelUuid + ")");
    }

    @Override
    public void deleteChannel(String channelUuid) {
        channelRepository.delete(channelUuid);
        System.out.println("Channel deleted: UUID " + channelUuid);
    }
}
