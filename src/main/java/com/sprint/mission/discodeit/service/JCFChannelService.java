package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService() {
        this.data = new ArrayList<>();
    }

    @Override
    public void createChannel(Channel channel) {
        data.add(channel);
    }

    @Override
    public boolean channelExits(Channel channel) {
        return data.contains(channel);
    }

    @Override
    public void deleteChannel(Channel channel, User admin) {
        boolean removed = data.removeIf(check -> check.equals(channel) && check.getAdmin().equals(admin));
        if (removed) {
            channel.deleteAllMessage();
            channel.deleteAllMember();
            System.out.println("채널이 삭제되었습니다.");
        } else {
            System.out.println("채널이 없거나, 관리자가 아닙니다.");
        }

    }

    @Override
    public void updateChannel(Channel channel, String name, User admin) {
        boolean adminCheck = data.stream()
                .filter(check -> check.equals(channel))
                .anyMatch(check -> check.getAdmin().equals(admin));

        if (!adminCheck) {
            System.out.println("관리자만 변경 할 수 있습니다.");
        } else {
            channel.setChannelName(name);
        }
    }

    @Override
    public List<Map<String, String>> getAllChannel() {
        return data.stream()
                .map(channel -> {
                    Map<String, String> channelInfo = new HashMap<>();
                    channelInfo.put("채널이름", channel.getChannelName());
                    channelInfo.put("관리자", channel.getAdmin().getUserName());
                    return channelInfo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllMember(Channel channel) {
        return channel.getMemberList().stream()
                .map(User::getUserName)
                .collect(Collectors.toList());
    }


}
