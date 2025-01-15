package com.sprint.mission.discodeit.jcf;


import project.entity.Channel;
import project.service.ChannelService;
import project.service.UserService;

import java.util.ArrayList;
import java.util.List;


public class JCFChannelService implements ChannelService {

    private static JCFChannelService instance; // 싱글톤 인스턴스
    private final List<Channel> channelList;
    private final UserService userService; // 의존성 주입

    private JCFChannelService(UserService userService) {
        this.channelList = new ArrayList<>();
        this.userService = userService;
    }

    public static synchronized JCFChannelService getInstance(UserService userService) {
        if (instance == null) {
            instance = new JCFChannelService(userService);
        }
        return instance;
    }

    @Override
    public void createChannel(Channel channel) {
        for (Channel existingChannel : channelList) {
            if (existingChannel.getChannelUuid().equals(channel.getChannelUuid())) {
                System.out.println("Channel with UUID " + channel.getChannelUuid() + " already exists.");
                return;
            }
        }
        channelList.add(channel);
        System.out.println("Channel created: " + channel.getChannelTitle() + " (UUID: " + channel.getChannelUuid() + ")");
    }

    @Override
    public Channel readChannel(String channelUuid) {
        return channelList.stream()
                .filter(channel -> channel.getChannelUuid().toString().equals(channelUuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Channel> readAllChannels() {
        return new ArrayList<>(channelList);
    }

    @Override
    public void updateChannel(String channelUuid, String newChannelTitle) {
        Channel channel = readChannel(channelUuid);
        if (channel != null) {
            channel.updateUserName(newChannelTitle);
            System.out.println("Channel updated: " + channel.getChannelTitle() + " (UUID: " + channel.getChannelUuid() + ")");
        } else {
            System.out.println("Channel with UUID " + channelUuid + " not found.");
        }
    }

    @Override
    public void deleteChannel(String channelUuid) {
        Channel channel = readChannel(channelUuid);
        if (channel != null) {
            channelList.remove(channel);
            System.out.println("Channel with UUID " + channelUuid + " deleted.");
        } else {
            System.out.println("Channel with UUID " + channelUuid + " not found.");
        }
    }
}
