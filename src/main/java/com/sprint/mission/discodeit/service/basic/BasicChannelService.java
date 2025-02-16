package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel createPrivateChannel(ChannelDTO.PrivateChannelDTO privateChannelDTO) {
        Channel channel = new Channel(ChannelType.PRIVATE, privateChannelDTO.getName(), privateChannelDTO.getDescription());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPublicChannel(ChannelDTO.PublicChannelDTO publicChannelDTO) {
        Channel channel = new Channel(ChannelType.PUBLIC, publicChannelDTO.getName(), publicChannelDTO.getDescription());
        return channelRepository.save(channel);
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public Channel update(UUID channelId, ChannelDTO.UpdateChannelDTO updateChannelDTO) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        channel.update(updateChannelDTO.getNewName(), updateChannelDTO.getNewDescription());

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channelRepository.deleteById(channelId);
    }
}
