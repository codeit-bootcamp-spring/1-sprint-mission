package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String channelName, User admin) {
        Channel channel = new Channel(type, channelName, admin);
        return channelRepository.save(channel);
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newChannelName) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
        channel.update(newChannelName);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsId(channelId)) {
            throw new NoSuchElementException("채널이 존재하지 않습니다.");
        }
        channelRepository.delete(channelId);
    }

    @Override
    public void joinChannel(UUID channelId, User user) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
        channel.addMember(user);
        channelRepository.save(channel);
    }

    @Override
    public void leaveChannel(UUID channelId, User user) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
        channel.deleteMember(user);
        channelRepository.save(channel);
    }
}
