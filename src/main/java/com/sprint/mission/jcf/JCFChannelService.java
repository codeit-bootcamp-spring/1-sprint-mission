package com.sprint.mission.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channelData = new ArrayList<>();

    // 채널 생성
    @Override
    public Channel createChannel(User user, String channelName) {
        Channel newChannel = new Channel(user, channelName);
        channelData.add(newChannel);
        System.out.println(user + "님이 " + channelName + "에 " +
                "채널 생성 : " + newChannel + "\n채널이 성공적으로 생성되었습니다!");
        return newChannel;
    }

    // 채널명 변경
    @Override
    public void updateChannel(User user, String modifiedChannelName) {

        for (Channel channel : channelData) {
            if (channel.getUser().equals(user)) {
                channel.setChannelName(modifiedChannelName);
                System.out.println("채널명 변경 : " + modifiedChannelName + "\n채널명이 변경되었습니다.");
                return;
            }
        }
        System.out.println("채널을 찾을 수 없습니다.");
    }

    // 전체 채널 조회
    @Override
    public List<Channel> getAllChannelList() {
        System.out.println("채널 목록 = " + channelData);
        return new ArrayList<>(channelData);
    }

    // 유저가 속한 채널 조회
    @Override
    public void channelInfo(User user) {
        System.out.println(channelData);
    }

    // 채널 삭제, 채널을 찾을 수 없을 경우 예외 처리
    @Override
    public void deleteChannel(String channelName) {
        Channel channelToDelete = null;
        for (Channel channel : channelData) {
            if (channel.getChannelName().equals(channelName)) {
                channelToDelete = channel;
                break;
            }
        }
        if (channelToDelete != null && channelData.contains(channelToDelete)) {
            channelData.remove(channelToDelete);
            System.out.println("채널 삭제 : " + channelName + "\n채널이 성공적으로 삭제되었습니다.");
        } else {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
        }
    }
}
