package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.view.ConsoleView;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    private final ConsoleView consoleView;

    @Override
    public Channel createChannel(String channelname, String description) {
        Channel channel = new Channel(channelname, description);
        channelRepository.save(channel);
        consoleView.displaySuccess(channel.getChannelName());
        return channel;
    }

    @Override
    public Channel getChannelById(UUID channelId) {
        Channel channel = channelRepository.findByChannelId(channelId);
        if (channel != null) {
            consoleView.displayChannel(channel, null);
        }
        return channel;
    }

    @Override
    public List<Channel> getAllChannels() {
        List<Channel> channels = channelRepository.findAll();
        consoleView.displayChannels(channels);
        return channels;
    }

    @Override
    public Channel updateChannelName(UUID channelId, String newName) {
        Channel channel = getChannelById(channelId);
        if (channel != null) {
            channel.updateChannelName(newName);
            channelRepository.save(channel);
            consoleView.displaySuccess("채널 이름이 업데이트되었습니다: " + newName);
        } else {
            consoleView.displayError("채널을 찾을 수 없습니다");
        }
        return channel;
    }

    @Override
    public Channel updateDescription(UUID channelId, String newDescription) {
        Channel channel = getChannelById(channelId);
        if (channel != null) {
            channel.updateDescription(newDescription);
            channelRepository.save(channel);
            consoleView.displaySuccess("채널 설명이 업데이트되었습니다");
        } else {
            consoleView.displayError("채널을 찾을 수 없습니다");
        }
        return channel;
    }


    @Override
    public boolean deleteChannel(UUID channelId) {
        Channel channel = getChannelById(channelId);
        if (channel != null) {
            channelRepository.deleteById(channelId);
            consoleView.displaySuccess("채널이 삭제되었습니다");
            return true;
        }
        consoleView.displayError("채널 삭제에 실패했습니다");
        return false;
    }
}