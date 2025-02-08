package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    //서비스 로직
    @Override
    public UUID create(String name, String description, ChannelType type) {
        if (!Channel.validation(name, description)) {
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        Channel channel = new Channel(name, description, type);
        return channelRepository.save(channel);
    }

    @Override
    public Channel read(UUID id) {
        Channel findChannel = channelRepository.findOne(id);
        return Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    public Channel updateChannel(UUID id, String name, String description, ChannelType type){
        Channel findChannel = channelRepository.findOne(id);
        findChannel.setChannel(name, description, type);
        channelRepository.update(findChannel);
        return findChannel;
    }

    @Override
    public UUID deleteChannel(UUID id) {
        Channel findChannel = channelRepository.findOne(id);
        return channelRepository.delete(findChannel.getId());
    }
}
