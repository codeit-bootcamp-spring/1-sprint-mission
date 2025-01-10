package com.sprint.mission.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.service.jcf.EntityService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


public class ChannelRepository{
    private static final ChannelRepository INSTANCE = new ChannelRepository();
    private ChannelRepository(){}

    List<Channel> channels = new ArrayList<>();

    public static ChannelRepository getInstance() {
        return INSTANCE;
    }

    public Channel createChannel(String channelName, UUID userId, String userName){
        Channel channel = new Channel();
        channel.setUpdatedAt(System.currentTimeMillis());
        channel.setName(channelName);
        channel.setUsers(userId,userName);
        channels.add(channel);
        return channel;
    }

    public void joinChannel(UUID channelId, UUID userId, String userName){
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                channel.setUsers(userId,userName);
            }
        }
    }

    public void deleteChannel(UUID deleteChannelId){
        for(Channel channel : channels){
            if(channel.getChannelId().equals(deleteChannelId)){
                channels.remove(channel);
                break;
            }
        }
    }

    public void checkJoinChannel(UUID checkingUserId, String newName){
        List<Channel> joinedChannel = new ArrayList<>();
        for(Channel channel : channels){
            if(channel.getUsers().containsKey(checkingUserId)){
                joinedChannel.add(channel);
            }
        }

        modifyChannelUser(joinedChannel, checkingUserId, newName);
    }

    private void modifyChannelUser(List<Channel> joinedChannel, UUID checkUserId, String newName) {
        for(Channel channel : channels){
            if(joinedChannel.contains(channel)){
                channel.getUsers().put(checkUserId,newName);
            }
        }
    }

    public boolean getChannelId(UUID id){
        for(Channel channel : channels){
            if(channel.getChannelId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public boolean checkChannelContains(UUID channelId){
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                return true;
            }
        }

        return false;
    }

    public void modifyChannel(UUID channelId, String name) {
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                channel.setName(name);
                channel.setUpdatedAt(System.currentTimeMillis());
            }
        }
    }

    public Collection<String> joinUserList(UUID channelId){
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                return channel.getUsers().values();
            }
        }

        return new ArrayList<>();
    }

    public void deleteUser(UUID id) {
        for(Channel channel : channels){
            channel.getUsers().remove(id);
        }
    }
}
