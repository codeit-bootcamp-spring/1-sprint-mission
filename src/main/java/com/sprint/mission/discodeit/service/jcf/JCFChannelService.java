package com.sprint.mission.discodeit.service.jcf;


import java.util.List;
import java.util.ArrayList;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;


public class JCFChannelService implements ChannelService {
    private final ArrayList<Channel> data;

    public JCFChannelService() {
        data = new ArrayList<>();
    }

    // 채널 생성
    public Channel createChannel(String channelName, String description) {
        Channel channel = new Channel(channelName, description);
        data.add(channel);
        System.out.println(channel.getChannelName() + " channel created");
        return channel;
    }

    // 채널 정보 수정
    public Channel updateName(Channel channel, String name) {
        channel.updateChannelName(name);
        System.out.println("channel name updated");
        return channel;
    }
    public Channel updateDescription(Channel channel, String description) {
        channel.updateDescription(description);
        System.out.println("channel description updated");
        return channel;
    }

    // 채널 조회
    public Channel findChannel(Channel channel) {
        for (Channel c : data) {
            if (c.getId().equals(channel.getId())) {
                System.out.println("channel found");
                return c;
            }
        }
        System.out.println("channel not found");
        return null;
    }
    public List<Channel> findAllChannels() {
        return data;
    }

    // 채널 정보 프린트
    public void printChannel(Channel channel) {
        System.out.println(channel);
    }
    public void printAllChannels(List<Channel> channels) {
        channels.forEach(System.out::println);
    }

    // 채널 삭제
    public void deleteChannel(Channel channel) {
        System.out.println(channel.getChannelName() + " channel deleted");
        data.remove(channel);
    }

}