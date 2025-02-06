package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    // 모든 채널 객체가 담기는 해쉬맵
    private static final HashMap<UUID, Channel> channelsMap = new HashMap<UUID, Channel>();
    // 외부에서 생성자 접근 불가
    private JCFChannelRepository() {}
    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class JCFChannelRepositoryHolder {
        private static final JCFChannelRepository INSTANCE = new JCFChannelRepository();
    }
    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static JCFChannelRepository getInstance() {
        return JCFChannelRepositoryHolder.INSTANCE;
    }




    // 모든 채널 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return channelsMap;
    }

    // 특정 채널객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Channel getChannel(UUID channelId) {
        if (channelId == null || channelsMap.containsKey(channelId)==false ) {
            return null;
        }
        return channelsMap.get(channelId);
    }

    // 특정 채널객체 여부 확인 후 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelsMap.containsKey(channelId) == false || channelId == null) {
            return false;
        }
        channelsMap.remove(channelId);
        return true;
    }

    // 전달받은 채널객체 null 여부 확인 후 채널 해쉬맵에 추가.
    @Override
    public boolean addChannel(Channel channel) {
        if (channel == null) {
            return false;
        }
        channelsMap.put(channel.getId(), channel);
        return true;
    }

    // 채널 존재여부 반환
    @Override
    public boolean isChannelExist(UUID channelId) {
        if (channelId == null || channelsMap.containsKey(channelId) == false) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addChannelMember(UUID channelId, User member){;
        if (channelId==null || member==null) {
            return false;
        }
        channelsMap.get(channelId).getMembers().add(member);
        return true;
    }


}
