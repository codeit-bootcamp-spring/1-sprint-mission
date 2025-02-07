package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator channelValidator;

    @Override
    public Channel createChannel(Channel.ChannelType channelType, String title, String description) {
        if (channelValidator.isValidTitle(title)) {
            Channel newChannel = Channel.createChannel(channelType, title, description);
            channelRepository.save(newChannel);
            log.info("Create Channel: {}", newChannel);
            return newChannel;
        }
        return null;
    }

    @Override
    public List<Channel> getAllChannelList() {
        return channelRepository.findAll();
    }

    @Override
    public Channel searchById(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Channel does not exist"));
    }

    @Override
    public void updateChannel(UUID id, String title, String description) {
        Channel channel = searchById(id);
        if (channelValidator.isValidTitle(title) && channelValidator.isValidTitle(description)) {
            channel.update(title, description);
            channelRepository.save(channel);
            log.info("Update Channel : {}", channel);
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteById(channelId);
    }

}
