package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

import static com.sprint.mission.discodeit.util.MyLogger.log;

public class JcfChannelService implements ChannelService {

    private final Map<UUID,Channel> data=new HashMap<>();
    @Override
    public void createChannel(Channel channel) {
        if (channel.getChannelName().trim().isEmpty()) {
            log("채널 이름을 입력해주세요.");
            return;
        }
        if (channel.getDescription().trim().isEmpty()) {
            log("채널 설명을 입력해주세요.");
            return;
        }
        data.put(channel.getId(),channel);
    }

    @Override
    public Optional<Channel> findChannel(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAllChannels() {
        List<Channel> channelList=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            channelList.add(data.get(uuid));
        }
        return channelList;
    }

    @Override
    public void updateChannelName(UUID id, String channelName) {
        if(data.containsKey(id)) {
            data.get(id).updateChannelName(channelName);
        }
        /*for (Channel channel : data) {
            if(channel.getId().equals(updateChannel.getId())){
                channel.updateChannelName(updateChannel.getChannelName());
                channel.updateDescription(updateChannel.getDescription());
            }
        }*/
    }
    public void updateChannelDescription(UUID id, String channelContent) {
        if (data.containsKey(id)) {
            data.get(id).updateDescription(channelContent);
        }
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

