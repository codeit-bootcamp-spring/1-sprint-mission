package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;

public class JCFChannelService implements ChannelService {

    //생성자 접근 불가능하도록 함.
    private JCFChannelService() {
    }

    //모든 채널객체가 담기는 해쉬맵 싱글톤 객체 'channelsMap' 생성. LazyHolder 방식으로 스레드세이프 보장
    private static class JCFChannelServiceHolder {
        private static final JCFChannelService INSTANCE = new JCFChannelService();
        private static final HashMap<UUID, Channel> channelsMap = new HashMap<UUID, Channel>();
    }

    public static JCFChannelService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return JCFChannelServiceHolder.INSTANCE;
    }

    //모든 채널 관리 맵 'channelsMap' 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return JCFChannelServiceHolder.channelsMap;
    }

    //채널 생성. 'channelsMap'에 uuid-채널객체 주소 넣어줌. 성공여부 리턴
    @Override
    public UUID createChannel(String channelName) {
        if (channelName == null){
            return null;
        }
        Channel newChannel = new Channel(channelName);
        getChannelsMap().put(newChannel.getId(), newChannel);
        return newChannel.getId();
    }

    //channelsMap에 해당 id 존재여부 리턴
    @Override
    public boolean isChannelExist(UUID channelId) {
        return getChannelsMap().containsKey(channelId);
    }

    //UUID를 통해 채널 객체를 찾아 채널주소 리턴. 없으면 null
    @Override
    public Channel getChannel(UUID channelId) {
        if (channelId == null){
            return null;
        }
        return getChannelsMap().get(channelId);
    }

    //UUID를 통해 채널 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteChannel(UUID channelId) {
        if (channelId == null){
            return false;
        }
        getChannelsMap().remove(channelId);
        return true;
    }

    //채널명 변경. 성공여부 반환
    @Override
    public boolean changeChannelName(UUID channelId, String newName) {
        if (channelId == null || newName == null){
            return false;
        }
        getChannel(channelId).setChannelName(newName);
        return true;

    }

    //채널에 속한 멤버를 전부 교체. 성공여부 리턴
    @Override
    public boolean changeChannelMembers(UUID channelId, ArrayList<User> members){
        if (isChannelExist(channelId)==false || members==null || members.isEmpty()){
            return false;
        }
        getChannel(channelId).setMembers(members);
        return true;
    }

    //멤버 하나 추가. 성공여부 리턴.
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        if (isChannelExist(channelId)==false || memberId==null) {
            return false;
        }
        getChannel(channelId).addMember(JCFUserService.getInstance().getUser(memberId));
        return true;
    }
}


