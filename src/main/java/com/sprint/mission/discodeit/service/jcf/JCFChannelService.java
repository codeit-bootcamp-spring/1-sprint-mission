package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final HashMap<UUID, Channel> data = new HashMap<>();
    private final ObserverManager observerManager;

    public JCFChannelService(ObserverManager observerManager) {
        this.observerManager = observerManager;

    }
    @Override
    public Channel createChannel(String chName){
        Channel ch1 = new Channel(chName);
        data.put(ch1.getuuId(), ch1);
        return ch1;
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
        Channel channel = data.get(uuId);
        if (channel != null) {
            channel.update(name);
        }
    }
    @Override
    public void deleteChannel(UUID id){
        data.remove(id);
        observerManager.channelDeletionEvent(id);
    }
}
