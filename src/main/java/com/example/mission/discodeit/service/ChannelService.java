package com.example.mission.discodeit.service;

import com.example.mission.discodeit.entity.Channel;
import com.example.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Channel updateChannel(Long id, Channel channelDetails) {
        return channelRepository.findById(id).map(channel -> {
            channel.setName(channelDetails.getName());
            channel.setPrivate(channelDetails.isPrivate());
            return channelRepository.save(channel);
        }).orElseThrow(() -> new RuntimeException("Channel not found"));
    }

    public void deleteChannel(Long id) {
        channelRepository.deleteById(id);
    }

    public List<Channel> getUserChannels(UUID userId) {
        return channelRepository.findByUserId(userId);
    }
}
