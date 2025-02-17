package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.*;

@ConditionalOnProperty(name = "app.channel-repository", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

    // 모든 채널 객체가 담기는 해쉬맵
    private static final HashMap<UUID, Channel> channelsMap = new HashMap<UUID, Channel>();

    // 모든 채널 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return channelsMap;
    }

    // 특정 채널객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Channel getChannel(UUID channelId) {
        return channelsMap.get(channelId);
    }

    // 특정 채널객체 여부 확인 후 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        return channelsMap.remove(channelId) != null;
    }


    // 전달받은 채널객체 null 여부 확인 후 채널 해쉬맵에 추가.
    @Override
    public boolean saveChannel(Channel channel) {
        channelsMap.put(channel.getId(), channel);
        return true;
    }

    // 채널 존재여부 반환
    @Override
    public boolean isChannelExist(UUID channelId) {
        return channelsMap.containsKey(channelId);
    }

    //채널 멤버 추가
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        channelsMap.get(channelId).getMembers().add(memberId);
        return true;
    }

    //멤버 삭제
    @Override
    public boolean removeChannelMember(UUID channelId, UUID memberId) {
        Channel channel = channelsMap.get(channelId);
        List<UUID> membersId = channel.getMembers();
        int index = membersId.indexOf(memberId);
        if (index == -1){return false;}
        membersId.remove(index);
        return true;
    }


}
