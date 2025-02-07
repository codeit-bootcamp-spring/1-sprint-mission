package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository=channelRepository;
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        return channelRepository.save(channel);
    }

    @Override
    public Channel readChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAllChannel() {
        List<Channel> channelList = new ArrayList<>(channelRepository.load().values());
        return channelList;
    }

    @Override
    public Channel modifyChannel(UUID id, String name) {
        Channel channel = channelRepository.findById(id);
        channel.updateName(name);
        return channelRepository.save(channel);
        //리포지토리에서 객체를 불러와 이름 변경, 레포지토리에 저장.
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.delete(id);
    }
}
