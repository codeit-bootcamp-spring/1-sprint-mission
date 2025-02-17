package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final HashMap<UUID, Channel> data = new HashMap<>();
    private final ChannelValidator channelValidator;


    public JCFChannelService(ChannelValidator channelValidator) {
        this.channelValidator = channelValidator;
    }
    @Override
    public Channel createPublicChannel(String name, String description){
        try {
            channelValidator.isUniqueName(name);
        }catch (CustomException e){
            System.out.println("Channel 생성 실패 -> "+ e.getMessage());
        }
        Channel ch = new Channel(ChannelType.PUBLIC,name, description);
        data.put(ch.getId(), ch);
        return ch;
    }
    @Override
    public Channel createPrivateChannel(String name, String description){
        try {
            channelValidator.isUniqueName(name);
        }catch (CustomException e){
            System.out.println("Channel 생성 실패 -> "+ e.getMessage());
        }
        Channel ch = new Channel(ChannelType.PRIVATE,name, description);
        data.put(ch.getId(), ch);
        return ch;
    }

    @Override
    public Channel find(UUID id){
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID uuId, String name, String description ){
        Channel channel = find(uuId);
        if (channel != null) {
            channel.update(name, description);
        }
        return null;
    }

    @Override
    public void delete(UUID uuid){
        data.remove(uuid);
    }
}