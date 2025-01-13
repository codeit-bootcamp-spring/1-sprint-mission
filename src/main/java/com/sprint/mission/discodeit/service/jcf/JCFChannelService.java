package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService (){
        this.data = new ArrayList<>();
    }

    @Override
    public Channel createChannel(UUID id, String username, String channelName){
        Channel channel = new Channel(id,username,channelName);
        data.add(channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> allChannel(){
        return new ArrayList<>(data);
    }

    @Override
    public List<Channel> searchChannelsByName(String keyword){
        List<Channel> result = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            Channel channel = data.get(i);
            if(channel.getChannelName().toLowerCase().contains(keyword.toLowerCase()));
            result.add(channel);
        }
        return result;
    }

    @Override
    public void updateChannel(UUID id, String newChannelName) {
        Channel channel = getChannel(id);
        if (channel != null) {
            channel.setChannelName(newChannelName);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        Channel channel = getChannel(id);
        if (channel != null) {
            data.remove(channel);
        }
    }

    @Override
    public void deleteAllChannel(){
        data.clear();
    }

    @Override
    public void deleteChannelsByUser(String userName){
        data.removeIf(channel -> channel.getUserName().equalsIgnoreCase(userName));
    }


}
