package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.validation.ChannelValidtor;

import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final ObserverManager observerManager;
    private final ChannelValidtor channelValidtor;
    private final ChannelRepository jcfChannelRepository;
    public JCFChannelService(ObserverManager observerManager, ChannelValidtor channelValidtor, ChannelRepository jcfCannelRepository) {
        this.observerManager = observerManager;
        this.channelValidtor = channelValidtor;
        this.jcfChannelRepository = jcfCannelRepository;
    }
    @Override
    public Channel createChannel(String chName){
        channelValidtor.isUniqueName(chName, jcfChannelRepository.findAll());
        Channel ch1 = new Channel(chName);
        jcfChannelRepository.save(ch1);
        return ch1;
    }
    @Override
    public Channel getChannel(UUID uuid){
        return jcfChannelRepository.findById(uuid);
    }

    @Override
    public Map<UUID, Channel> getAllChannels() {
        return jcfChannelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID uuId, String name ){
        Channel channel = jcfChannelRepository.findById(uuId);
        if (channel != null) {
            channel.update(name);
        }
    }
    @Override
    public void deleteChannel(UUID id){
        jcfChannelRepository.delete(id);
        observerManager.channelDeletionEvent(id);
    }
}