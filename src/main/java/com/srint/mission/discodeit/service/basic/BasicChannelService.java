package com.srint.mission.discodeit.service.basic;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.ChannelType;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.ChannelRepository;
import com.srint.mission.discodeit.service.ChannelService;

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

    @Override
    public Channel updateName(UUID id, String channelName) {
        Channel findChannel = channelRepository.findOne(id);
        findChannel.setName(channelName);
        channelRepository.update(findChannel);
        return findChannel;
    }

    @Override
    public Channel updateDescription(UUID id, String description){
        Channel findChannel = channelRepository.findOne(id);
        findChannel.setDescription(description);
        channelRepository.update(findChannel);
        return findChannel;
    }

    @Override
    public Channel updateType(UUID id, ChannelType type){
        Channel findChannel = channelRepository.findOne(id);
        findChannel.setType(type);
        channelRepository.update(findChannel);
        return findChannel;
    }

    @Override
    public UUID deleteChannel(UUID id) {
        Channel findChannel = channelRepository.findOne(id);
        return channelRepository.delete(findChannel.getId());
    }
}
