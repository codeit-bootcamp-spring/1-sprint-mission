package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final HashMap<UUID, Channel> data = new HashMap<>();


    public void addChannel(Channel channel){

        data.put(channel.getuuId(), channel);
    }


    public Channel getChannel(UUID uuid){
        return data.get(uuid);
    }


    public HashMap<UUID, Channel> getAllChannels(){
        return new HashMap<>(data);
    }


    public void updateChannel(UUID uuId, String name ){
        for(Channel channel:data.values()){
            if(channel.getuuId().equals(uuId)){
                channel.update(name);
                break;
            }
        }
    }


    public void deleteChannel(UUID id){
        data.remove(id);
    }
}
