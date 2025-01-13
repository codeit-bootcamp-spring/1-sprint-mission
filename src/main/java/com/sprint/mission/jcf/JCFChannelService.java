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
        System.out.println("채널 생성 성공");
        return newChannel;
    }

    // 채널명 변경
    @Override
    public void updateChannel(User user, String channelName, String modifiedChannelName) {

        for (Channel channel : channelData) {
            if (channel.getUser().equals(user)&& channel.getChannelName().equals(channelName)) {
                channel.setChannelName(modifiedChannelName);
                System.out.println("채널명 변경 성공");
                printChannel(channel);
                return;
            }
        }
        throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
    }

    // 전체 채널 조회
    @Override
    public List<Channel> getAllChannelList() {
        for (Channel channel : channelData) {
            System.out.println("채널 전체 조회");
            printChannel(channel);
        }
        return channelData;
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
            System.out.println("채널 삭제 성공");
            printChannel(channelToDelete);
        } else {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
        }
    }

    // 채널 출력 포멧팅
    private void printChannel(Channel channel) {
        System.out.println(" - 사용자: " + channel.getUser().getEmail());
        System.out.println(" - 채널: " + channel.getChannelName());
        System.out.println(" - 생성 시간: " + channel.getCreatedAt() + "\n");
    }
}
