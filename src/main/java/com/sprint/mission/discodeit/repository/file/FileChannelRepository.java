package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainChannelRepository = "Channel\\mainOIChannelRepository";

    // I/O로 생성된 모든 채널 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Channel> getChannelsMap() {
        return (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
    }

    // 특정 채널객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Channel getChannel(UUID channelId){
        HashMap<UUID, Channel> channelsMap = getChannelsMap();
        return channelsMap.get(channelId);
    }

    // 특정 채널객체 삭제
    @Override
    public boolean deleteChannel(UUID channelId){
        HashMap<UUID, Channel> channelsMap = getChannelsMap();
        //해시맵은 존재하는 key를 삭제하면 삭제한 요소를 반환하지만 없는 key를 삭제하면 null 반환.
        if (channelsMap.remove(channelId)==null){
            return false;
        };
        return fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
    }

    // 전달받은 채널객체 채널 해쉬맵에 추가.
    @Override
    public boolean saveChannel(Channel channel){
        if (channel == null) {
            System.out.println("addChannel()의 파라미터에 전달된 유저가 null인 상태입니다. ");
        }
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        channelsMap.put(channel.getId(), channel);
        return fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
    }

    // 채널 존재여부 반환
    @Override
    public boolean isChannelExist(UUID channelId) {
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        return channelsMap.containsKey(channelId);
    }

    // 채널 객체에 멤버 추가
    @Override
    public boolean addChannelMember(UUID channelId, UUID memberId){
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        Channel channel = channelsMap.get(channelId);
        ArrayList<UUID> membersId = channel.getMembers();
        if (membersId.contains(membersId)==true){
            System.out.println("해당 멤버는 이미 존재합니다.");
            return false;
        }
        channel.getMembers().add(memberId);
        return fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
    }


    //멤버 삭제
    @Override
    public boolean removeChannelMember(UUID channelId, UUID memberId) {
        HashMap<UUID, Channel> channelsMap = (HashMap<UUID, Channel>) fileIOHandler.deserializeHashMap(mainChannelRepository);
        if (channelsMap==null||isChannelExist(channelId)==false||channelsMap.get(memberId)==null){
            return false;
        }
        channelsMap.get(channelId).getMembers().remove(memberId);
        return fileIOHandler.serializeHashMap(channelsMap, mainChannelRepository);
    }


}
