package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        data = new HashMap<>();
    }

    // 채널 생성
    @Override
    public Channel createChannel(String channelName, User owner) {
        if (correctTitle(channelName)) {
            Channel newChannel = new Channel(channelName, owner);
            data.put(newChannel.getId(), newChannel);
            System.out.println(owner.getName() + "님께서 새로운 채널을 생성했습니다.");
            return newChannel;
        }
        return null;
    }

    // 모든 채널 조회
    @Override
    public List<Channel> getAllChannelList() {
        return data.values().stream().collect(Collectors.toList());
    }

    // 채널id로 조회
    @Override
    public Channel searchById(UUID channelId) {
        Channel channel = data.get(channelId);
        if (channel == null) {
            System.out.println("해당 채널이 없습니다.");
        }
        return channel;
    }

    //채널 정보 출력
    @Override
    public void printChannelInfo(Channel channel) {
        System.out.println(channel);
    }

    //채널 정보 - 여러건 출력
    @Override
    public void printChannelListInfo(List<Channel> channelList) {
        for (Channel channel : channelList) {
            printChannelInfo(channel);
        }
    }

    // Update ChannelName
    @Override
    public void updateTitle(Channel channel,String channelName){
        if (correctTitle(channelName)) {
            channel.updateTitle(channelName);
            System.out.println("수정되었습니다.");
        }
    }

    // Delete Channel
    @Override
    public void deleteChannel(Channel channel) {
        data.remove(channel.getId());
    }

    // 모든 User 출력하기
    @Override
    public List<User> getAllMemberList(Channel channel) {
        return channel.getMemberList();
    }

    // Add User
    @Override
    public void addMember(Channel channel, User user) {
        if (channel.getMemberList().contains(user)) { //멤버 있는지 확인 유무
            System.out.println("유저가 이미 이 채널을 가지고 있습니다.");
        } else {
            channel.addMember(user);
            user.addChannel(channel);
        }
    }

    // Delete User
    @Override
    public void deleteMember(Channel channel, User user) {
        if (channel.getMemberList().contains(user)) {
            channel.removeMember(user);
            user.removeChannel(channel);
            System.out.println(user.getName() + " 가 채널에서 나갔습니다.");
        } else {
            System.out.println("해당 멤버가 존재하지 않습니다.");
        }
    }

    private boolean correctTitle(String channelName) {
        if (channelName.isBlank()) {
            System.out.println("제목을 입력해주세요");
            return false;
        }
        return true;
    }
}
