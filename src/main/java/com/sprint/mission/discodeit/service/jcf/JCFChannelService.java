package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService() {
        data = new ArrayList<>();
    }

    // 채널 생성
    @Override
    public Channel createChannel(String title, User owner) {
        // title 검사하기
        if (isValidTitle(title)) {
            Channel newChannel = new Channel(title, owner);
            data.add(newChannel);
            System.out.println(owner.getName() + " create new channel");
            return newChannel;
        }
        return null;
    }

    // 모든 채널 조회
    @Override
    public List<Channel> getAllChannelList() {
        return data;
    }

    // 채널id로 조회
    @Override
    public Channel searchById(UUID channelId) {
        for (Channel channel : data) {
            if (channel.getId().equals(channelId)) {
                return channel;
            }
        }
        System.out.println("Channel does not exist");
        return null;
    }

    @Override
    public void printChannelInfo(Channel channel) {
        System.out.println(channel);
    }

    @Override
    public void printChannelListInfo(List<Channel> channelList) {
        for (Channel channel : channelList) {
            printChannelInfo(channel);
        }
    }

    // 채널명 업데이트
    @Override
    public void updateTitle(Channel channel,String title){
        if (isValidTitle(title)) {
            channel.updateTitle(title);
            System.out.println("success update");
        }
    }

    // 채널 삭제
    @Override
    public void deleteChannel(Channel channel) {
        data.remove(channel);
    }

    // 채널의 모든 멤버
    @Override
    public List<User> getAllMemberList(Channel channel) {
        return channel.getMemberList();
    }

    // 채널 멤버 추가
    @Override
    public void addMember(Channel channel, User user) {
        // 이미 멤버가 있는지 확인 후 추가
        if (channel.getMemberList().contains(user)) {
            System.out.println("user's already a channel member");
        } else {
            channel.addMember(user);
            user.addChannel(channel);
        }
    }

    // 채널 멤버 삭제
    @Override
    public void deleteMember(Channel channel, User user) {
        if (channel.getMemberList().contains(user)) {
            channel.removeMember(user);
            user.removeChannel(channel);
            System.out.println("success delete");
        } else {
            System.out.println("member does not exist");
        }
    }

    private boolean isValidTitle(String title) {
        if (title.isBlank()) {
            System.out.println("title must not be blank");
            return false;
        }
        return true;
    }
}
