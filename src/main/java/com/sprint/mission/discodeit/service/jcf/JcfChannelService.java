package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

import static com.sprint.mission.discodeit.util.MyLogger.log;

public class JcfChannelService implements ChannelService {

    private final ChannelRepository channelRepository = new JcfChannelRepository();

    @Override
    public void createChannel(Channel channel) {
        if (channel.getChannelName().trim().isEmpty()) {
            log("채널 이름을 입력해주세요.");
            return;
        }
        if (channel.getDescription().trim().isEmpty()) {
            log("채널 설명을 입력해주세요.");
            return;
        }
        channelRepository.createChannel(channel.getId(), channel);
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
        if (channelRepository.findById(id).isPresent()) {
            channelRepository.updateChannelName(id, channelName);
        }else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }

    public void updateChannelDescription(UUID id, String channelContent) {
        if (channelRepository.findById(id).isPresent()) {
            channelRepository.updateDescript(id, channelContent);
        }else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        if (channelRepository.findById(id).isPresent()) {
            channelRepository.deleteChannel(id);
        }else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }
}

