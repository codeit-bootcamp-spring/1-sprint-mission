package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final HashMap<UUID, Channel> data = new HashMap<>();


    public Channel createChannel(String chName){
        Channel ch1 = new Channel(chName);
        data.put(ch1.getuuId(), ch1);
        return ch1;
    }


    public Channel getChannel(UUID uuid){
        return data.get(uuid);
    }


    public Map<UUID, Channel> getAllChannels() {
        return new HashMap<>(data);
    }



    public void updateChannel(UUID uuId, String name ){
        Channel channel = data.get(uuId);
        if (channel != null) {
            channel.update(name);
        }
    }

    public void deleteChannel(UUID id){
        data.remove(id);
    }

}
