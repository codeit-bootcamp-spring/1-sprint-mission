package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository=new FileChannelRepository();

    @Override
    public void createChannel(Channel channel) {
        channelRepository.createChannel(channel.getId(),channel);
    }

    @Override
    public Optional<Channel> findChannel(UUID id) {
       return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannelName(UUID id, String channelName) {
        if(channelRepository.findById(id).isPresent()) {
           channelRepository.updateChannelName(id,channelName);
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
    @Override
    public void updateChannelDescription(UUID id, String channelContent) {
        if(channelRepository.findById(id).isPresent()){
           channelRepository.updateDescript(id,channelContent);
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        if(channelRepository.findById(id).isPresent()){
           channelRepository.deleteChannel(id);
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
