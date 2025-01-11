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

    //특정 id 가진 채널 조회
    public Channel getChannel(UUID id){
        return data.get(id);
    }

    //모든 채널 조회
    public HashMap<UUID, Channel> getAllChannels(){
        return new HashMap<>(data);
    }

    //채널 수정
    public void updateChannel(UUID uuId, String name ){
        for(Channel channel:data.values()){
            if(channel.getuuId().equals(uuId)){
                channel.update(name);
                break;
            }
        }
    }

    //채널 삭제
    public void deleteChannel(UUID id){
        data.remove(id);
    }
}
