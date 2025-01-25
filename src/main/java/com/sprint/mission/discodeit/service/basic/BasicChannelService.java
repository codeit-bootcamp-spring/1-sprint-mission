package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    //생성
    public Channel createChannel(String channelName, String description) {
        Channel channel = new Channel(channelName, description);
        if (channelRepository.saveChannel(channel)) {
            System.out.println(channel.getChannelName() + " channel created");
            return channel;
        }
        return null;
    }

    // 수정
    public Channel updateName(Channel channel, String name) {
        Channel updatedChannel = channelRepository.loadChannel(channel);
        if (updatedChannel != null) {
            updatedChannel.updateChannelName(name);
            if (channelRepository.saveChannel(updatedChannel)) {
                System.out.println("channel name updated");
                return updatedChannel;
            }
        }
        return null;
    }
    public Channel updateDescription(Channel channel, String description) {
        Channel updatedChannel = channelRepository.loadChannel(channel);
        if (updatedChannel != null) {
            updatedChannel.updateDescription(description);
            if (channelRepository.saveChannel(updatedChannel)) {
                System.out.println("channel description updated");
                return updatedChannel;
            }
        }
        return null;
    }

    // 조회
    public Channel findChannel(Channel channel) {
        return channelRepository.loadChannel(channel);
    }
    public List<Channel> findAllChannels() {
        return channelRepository.loadAllChannels();
    }

    // 프린트
    public void printChannel(Channel channel) {
        System.out.println(channel);
    }
    public void printAllChannels(List<Channel> channels) {
        for (Channel channel : channels) {
            System.out.println(channel);
        }
    }

    // 삭제
    public void deleteChannel(Channel channel) {
        if (channelRepository.deleteChannel(channel)) {
            System.out.println(channel.getChannelName() + " channel deleted");
        }
    }
}
