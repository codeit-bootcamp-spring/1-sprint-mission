package com.sprint.mission.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {
    private static JCFChannelService instance;

    private final List<Channel> channelData = new ArrayList<>();

    private JCFChannelService()  {}

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }
        return instance;
    }




    // 채널 생성
    @Override
    public Channel createChannel(User user, String channelName) {
        Channel newChannel = new Channel(user, channelName);
        channelData.add(newChannel);
        System.out.println("\n***채널 생성 성공***");
        return newChannel;
    }

    // 채널명 변경
    @Override
    public void updateChannel(User user, String channelName, String modifiedChannelName) {

        for (Channel channel : channelData) {
            if (channel.getUser().equals(user) && channel.getChannelName().equals(channelName)
                    && !channel.getChannelName().equals(modifiedChannelName)) {
                channel.setChannelName(modifiedChannelName);
                System.out.println("\n***채널명 변경 성공***");
                System.out.println(channel);
                return;
            }
        }
        System.out.println("**채널을 찾을 수 없습니다.**\n");
    }

    // 전체 채널 조회
    @Override
    public List<Channel> getAllChannelList() {
        System.out.println("\n***채널 전체 조회***");
        for (Channel channel : channelData) {
            System.out.println(channel);
        }
        return channelData;
    }

    // 유저가 속한 채널 조회
    @Override
    public void channelInfo(User user) {
        System.out.println("\n***[채널 목록]***");
        for (Channel channel : channelData) {
            if (channel.getUser().equals(user)) {
                System.out.println(channel);
            }
        }
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
        if (channelToDelete != null) {
            channelData.remove(channelToDelete);
            System.out.println("\n***채널 삭제 성공***");
            System.out.println(channelToDelete);
        } else {
            System.out.println("**채널을 찾을 수 없습니다.**\n");
        }
    }

}
