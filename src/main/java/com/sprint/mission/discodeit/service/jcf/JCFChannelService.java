package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.validation.ChannelValidtor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final HashMap<UUID, Channel> data = new HashMap<>();
    private final ObserverManager observerManager;
    private final ChannelValidtor channelValidtor;


    public JCFChannelService(ObserverManager observerManager, ChannelValidtor channelValidtor) {
        this.observerManager = observerManager;
        this.channelValidtor = channelValidtor;
    }
    @Override
    public Channel createChannel(String chName){
        if(channelValidtor.isUniqueName(chName, getAllChannels())){
            Channel channel = new Channel(chName);
            data.put(channel.getChanneluuId(), channel);
            return channel;
        }
        throw new CustomException(ExceptionText.CHANNEL_CREATION_FAILED);
    }

    @Override
    public Channel getChannel(UUID uuid){
        return data.get(uuid);
    }

    @Override
    public Map<UUID, Channel> getAllChannels() {
        return new HashMap<>(data);
    }

    @Override
    public void updateChannel(UUID uuId, String name ){
        Channel channel = getChannel(uuId);
        if (channel != null) {
            channel.update(name);
        }
    }

    @Override
    public void deleteChannel(UUID uuid){
        data.remove(uuid);
        observerManager.channelDeletionEvent(uuid);
    }
}