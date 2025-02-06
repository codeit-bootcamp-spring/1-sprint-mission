package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public Channel createChannel(ChannelDto channelDto) {
        return channelRepository.save(Channel.of(channelDto.getName(), channelDto.getDescription()));
    }

    @Override
    public Channel readChannel(UUID channelId) {
        return channelRepository.findById(channelId);
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID channelId, ChannelDto channelDto) {
        Channel channel = channelRepository.findById(channelId);
        channel.updateName(channelDto.getName());
        channel.updateDescription(channelDto.getDescription());
        channelRepository.updateChannel(channel);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);
        channel.addUser(userRepository.findById(userId));
        channelRepository.updateChannel(channel);
    }

    @Override
    public void deleteUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);
        channel.deleteUser(userId);
        channelRepository.updateChannel(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteChannel(channelId);
    }
}