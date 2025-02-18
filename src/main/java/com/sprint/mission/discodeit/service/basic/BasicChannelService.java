package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    @Override
    public Channel createChannelPublic(ChannelDTO channelDTO) { //public 채널
        Channel channel = new Channel(channelDTO, ChannelType.PUBLIC);
        channelRepository.save(channel);
        return channel;
    }
    @Override
    public Channel createChannelPrivate(ChannelDTO channelDTO) { //private 채널
        Channel channel = new Channel(channelDTO, ChannelType.PRIVATE);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId).orElseThrow(()-> new NoSuchElementException("ChannelId : " + channelId + "를 찾을 수 없습니다."));
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> data = channelRepository.findAll();
        if(data.isEmpty()) return new ArrayList<>();
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public Channel update(UUID channelId, ChannelDTO channelDTO) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(()-> new NoSuchElementException("ChannelId :" + channelId + "를 찾을 수 없습니다"));
        channel.update(channelDTO);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void addUserInChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(()-> new NoSuchElementException("ChannelId : " + channelId + "를 찾을 수 없습니다."));
        if(channel.containUser(userId))return;
        channel.getUserId().add(userId);
        channelRepository.save(channel);

    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(()-> new NoSuchElementException("ChannelId : " + channelId + "를 찾을 수 없습니다."));
        channelRepository.delete(channelId);

    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("ChannelId : " + channelId + "를 찾을 수 없습니다."));
        if(!channel.containUser(userId))return;
        channel.getUserId().remove(userId);
        channelRepository.save(channel);

    }

    @Override
    public void deleteUserInAllChannel(UUID userId) {
        Map<UUID, Channel> data = channelRepository.findAll();
        if(data.isEmpty()) return;
        data.values().stream().forEach(channel -> {
            channel.getUserId().remove(userId);
            channelRepository.save(channel);
        });

    }
}
