package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    // 모든 채널 객체가 담기는 해쉬맵
    private static final HashMap<UUID, Channel> channelsMap = new HashMap<UUID, Channel>();

    // 모든 채널 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() throws Exception {
        return channelsMap;
    }

    // 특정 채널객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Channel getChannel(UUID channelId) {
        if (channelId == null) {
            return null;
        }
        return Optional.ofNullable(channelsMap.get(channelId)).orElseThrow(()->new NoSuchElementException("해당 uuid를 가진 채널이 존재하지 않습니다."));
    }

    // 특정 채널객체 여부 확인 후 삭제
    @Override
    public boolean deleteChannel(UUID channelId) {
        //해시맵은 존재하는 key를 삭제하면 삭제한 요소를 반환하지만 없는 key를 삭제하면 null 반환.
        if (channelsMap.remove(channelId)==null){
            throw new NoSuchElementException("해당 uuid를 가진 채널이 존재하지 않습니다.");
        };
        return true;
    }

    // 전달받은 채널객체 null 여부 확인 후 채널 해쉬맵에 추가.
    @Override
    public boolean saveChannel(Channel channel) {
        if (channel == null) {
            throw new RuntimeException("addChannel()의 파라미터에 전달된 유저가 null인 상태입니다. ");
        }
        channelsMap.put(channel.getId(), channel);
        return true;
    }

    // 채널 존재여부 반환
    @Override
    public boolean isChannelExist(UUID channelId) {
        return channelsMap.containsKey(channelId);
    }

    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){;
        if (channelId==null || memberId==null) {
            return false;
        }
        channelsMap.get(channelId).getMembers().add(memberId);
        return true;
    }

    //멤버 삭제
    //todo 멤버를 굳이 숫자가 있는 ArrayList에 넣어야할까? 컬렉션이 어레이리스트이다보니 요소 인덱스 찾은다음에 삭제해야돼서 불편한데? 멤버 리스트의 컬렉션종류 변경을 고려해봐야겠음.
    @Override
    public boolean removeChannelMember(UUID channelId, UUID memberId) throws Exception {
        Channel channel = Optional.ofNullable(channelsMap.get(channelId)).orElseThrow(()->new NoSuchElementException("해당 uuid를 가진 채널이 존재하지 않습니다."));
        List<UUID> membersId = channel.getMembers();
        int index = membersId.indexOf(memberId);
        if (index == -1){
            throw new NoSuchElementException("해당 uuid를 가진 유저가 존재하지 않습니다.");
        }
        membersId.remove(index);
        return false;
    }


}
