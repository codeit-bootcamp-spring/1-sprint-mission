package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    Map<UUID, Channel> channels;
    public JCFChannelRepository(){
        this.channels = new HashMap<>();
    }
    public void saveChannel(Channel channel){
        channels.put(channel.getId(), channel);
    }
    public Channel findChannelById(UUID id){
        return channels.get(id);
    }
    public Map<UUID,Channel> getAllChannels(){
        return channels;
    }
    public void deleteAllChannels(){
        channels.clear();
    }
    public void deleteChannelById(UUID id){
        channels.remove(id);
    }
}
