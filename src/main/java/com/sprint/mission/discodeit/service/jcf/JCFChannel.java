package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannel implements ChannelService {
    private final List<Channel> data;

    public JCFChannel() {
        data = new ArrayList<>();
    }

    // 채널 생성
    @Override
    public Channel createChannel(String title, User owner) {
        Channel newChannel = new Channel(title, owner);
        data.add(newChannel);
        return newChannel;
    }

    // 모든 채널 조회
    @Override
    public List<Channel> getAllChannelList() {
        return data;
    }

    // 채널명으로 조회
    @Override
    public Channel searchByTitle(String title) {
        for (Channel channel : data) {
            if (channel.getTitle().equals(title)) {
                return channel;
            }
        }
        System.out.println("존재하지 않는 채널입니다.");
        return null;
    }

    // ? 채널명 업데이트
    @Override
    public void updateTitle(Channel channel,String title){
        channel.updateTitle(title);
    }

    // 채널 삭제
    @Override
    public void deleteChannel(Channel channel) {
        data.remove(channel);
    }

    // 채널 정보 출력
    public void printChannelInfo(Channel channel) {
        System.out.println(channel.displayInfoChannel());
    }

    // ? 채널의 모든 멤버
    @Override
    public List<User> getAllMemberList(Channel channel) {
        return channel.getMemberList();
    }

    // 채널 멤버 추가
    @Override
    public void addMember(Channel channel, User user) {
        // 이미 멤버가 있는지 확인 후 추가
        if (channel.getMemberList().contains(user)) {
            System.out.println("이미 채널 멤버입니다.");
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
            System.out.println(user.getName() + "멤버가 삭제되었습니다.");
        } else {
            System.out.println("채널에 존재하지 않는 멤버입니다.");
        }
    }
}
