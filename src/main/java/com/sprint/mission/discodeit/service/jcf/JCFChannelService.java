package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final List<Channel> channels = new ArrayList<>();

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        channels.add(channel);
        return channel;
    }

    @Override
    public Channel getChannelById(UUID channelId) {
        for (Channel channel : channels) {
            if (channel.getId().equals(channelId)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> getAllChannels() {
        return channels;
    }
    // ======================================================================================================
    //단건 출력 메서드
    public void printSingleChannel(UUID channelId) {
        Channel channel = getChannelById(channelId);
        if (channel != null) {
            System.out.println("Channel Name : " + channel.getChannelName() +
                    ", UUID : " + channel.getId() +
                    ", CreatedAt : " + channel.getCreatedAt() +
                    ", Updated At: " + channel.getUpdatedAt());
        } else {
            System.out.println("해당 ID의 채널을 찾을 수 없습니다.");
        }
    }
    //다건 출력 메서드
    public void printAllChannels() {
        List<Channel> channels = getAllChannels();
        if (!channels.isEmpty()) {
            for (Channel channel : channels) {
                System.out.println("Channel Name : " + channel.getChannelName() +
                        ", UUID : " + channel.getId() +
                        ", CreatedAt : " + channel.getCreatedAt() +
                        ", Updated At: " + channel.getUpdatedAt());
            }
        } else {
            System.out.println("현재 등록된 채널이 없습니다.");
        }
    }
    // ======================================================================================================

    @Override
    public Channel updateChannelName(UUID channelId, String newName) {
        Channel channel = getChannelById(channelId);
        if (channel != null) {
            channel.updateChannelName(newName);
        }
        return channel;
    }


    @Override
    public boolean deleteChannel(UUID channelId) { //삭제의 경우 읽기 전용인 향상된 for문을 사용하면 오류 = 일반 for문을 사용하였다.
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(channelId)) {
                channels.remove(i);
                return true;
            }
        }
        return false;
    }

}
