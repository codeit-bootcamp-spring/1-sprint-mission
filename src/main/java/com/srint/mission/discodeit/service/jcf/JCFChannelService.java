package com.srint.mission.discodeit.service.jcf;


import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        data = new HashMap<>();
    }

    //db 로직
    @Override
    public UUID save(Channel channel) {
        data.put(channel.getId(), channel);
        return(channel.getId());
    }

    @Override
    public Channel findOne(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("조회할 Channel을 찾지 못했습니다.");
        }
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        if(data.isEmpty()){
            throw new IllegalArgumentException("Channel이 없습니다.");
        }
        return data.values().stream().toList();
    }

    @Override
    public UUID delete(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("삭제할 Channel을 찾지 못했습니다.");
        }
        data.remove(id);
        return id;
    }


    //서비스 로직
    @Override
    public UUID create(String channelName, User channelOwner) {
        Channel channel = new Channel(channelName, channelOwner);
        return save(channel);
    }

    @Override
    public Channel read(UUID id) {
        return findOne(id);
    }

    @Override
    public List<Channel> readAll() {
        return findAll();
    }

    @Override
    public Channel updateChannelName(UUID id, User user,String channelName) {
        Channel findChannel = findOne(id);
        if(!findChannel.getChannelOwner().userCompare(user)){
            throw new IllegalStateException("채널 수정 권한이 없습니다.");
        }
        findChannel.setChannelName(channelName);
        return findChannel;
    }

    @Override
    public Channel joinChannel(UUID id, User user) {
        Channel findChannel = findOne(id);
        findChannel.setJoinedUsers(user);
        return findChannel;
    }

    //특정 사용자 채널 탈퇴
    @Override
    public UUID exitChannel(UUID id, User user) {
        Channel findChannel = findOne(id);
        findChannel.deleteJoinedUser(user);
        user.deleteMyChannels(findChannel);
        return findChannel.getId();
    }

    //채널삭제 사용자 권한
    @Override
    public UUID deleteChannel(UUID id, User user) {
        Channel findChannel = findOne(id);
        if(!findChannel.getChannelOwner().userCompare(user)){
            throw new IllegalStateException("채널 삭제 권한이 없습니다.");
        }
        findChannel.getJoinedUsers().forEach(
                u -> u.deleteMyChannels(findChannel));
        return delete(id);
    }
}
