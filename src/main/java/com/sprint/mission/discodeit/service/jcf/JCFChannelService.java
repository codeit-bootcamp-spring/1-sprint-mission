package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidtor;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final HashMap<UUID, Channel> data = new HashMap<>();
    private final ChannelValidtor channelValidtor;


    public JCFChannelService(ChannelValidtor channelValidtor) {
        this.channelValidtor = channelValidtor;
    }

    @Override
    public Channel create(ChannelType type, String name, String description){
        if(channelValidtor.isUniqueName(name, findAll())){
            Channel channel = new Channel(ChannelType.PUBLIC,name,description);
            data.put(channel.getId(), channel);
            return channel;
        }
        throw new CustomException(ExceptionText.CHANNEL_CREATION_FAILED);
    }

    @Override
    public Channel find(UUID channelId){
        return data.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription){
        Channel channel =find(channelId);
        if (channel != null) {
            channel.update(newName,newDescription);
            return channel;
        }
        return null;
    }

    @Override
    public void delete(UUID channelId){
        data.remove(channelId);
    }
}