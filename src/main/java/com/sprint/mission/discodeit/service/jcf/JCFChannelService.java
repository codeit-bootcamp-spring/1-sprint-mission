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
            System.out.println(owner.getUserName() + "님께서 새로운 채널을 생성했습니다.");
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

    @Override
    public void updateChannelName(UUID channelId, String channelName) {
        Channel channel = data.get(channelId);
        if(channel == null) {
            System.out.println("해당 채널이 없습니다.");
            return;
        }
        if(correctTitle(channelName)) {
            channel.setChannelName(channelName);
            System.out.println("채널 이름이 성공적으로 변경되었습니다 : " + channelName);
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = data.remove(channelId);
        if(channel == null) {
            System.out.println("삭제할 채널이 없습니다.");
        } else {
            System.out.println("채널이 성공적으로 삭제 되었습니다 : " + channel.getChannelName());
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
