package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JcfChannelService implements ChannelService {

    private final Map<UUID,Channel> data=new HashMap<>();
    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(),channel);
    }

    @Override
    public Optional<Channel> channelList(UUID id) {
        return Optional.ofNullable(data.get(id));
        /*for (Channel channel : data) {
            if(channel.getId().equals(id)){
                return Optional.of(channel);
            }
        }
        return Optional.empty();*/
    }

    @Override
    public List<Channel> allChannelList() {
        List<Channel> channelList=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            channelList.add(data.get(uuid));
        }
        return channelList;
    }

    @Override
    public void updateChannel(UUID id, Channel updateChannel) {
        if(data.containsKey(id)) {
            data.put(id,updateChannel);
        }
        /*for (Channel channel : data) {
            if(channel.getId().equals(updateChannel.getId())){
                channel.updateChannelName(updateChannel.getChannelName());
                channel.updateDescription(updateChannel.getDescription());
            }
        }*/
    }

    @Override
    public void deleteChannel(UUID id) {
        if(data.containsKey(id)) {
            data.remove(id);
        }
        /*for(int i=0;i<data.size();i++){
            if(data.get(i).getId().equals(id)){
                data.remove(i);
            }*/
        /*for (Channel channel : data) {
            if(channel.getId().equals(id)){
                data.remove(channel);
        }*/
        }
    }

