package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public Channel createChannel(String name, String description) {
        Channel channel = new Channel(name, description);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel updateChannel(UUID id, String name, String description) {
        Channel channel = channelRepository.findById(id);
        if (channel != null) {
            channel.update(name, description);
            channelRepository.save(channel);
            return channel;
        }
        return null;
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.delete(id);
    }
}
