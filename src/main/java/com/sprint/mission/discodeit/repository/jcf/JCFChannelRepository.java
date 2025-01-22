package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    Map<UUID, Channel> channels;
    public JCFChannelRepository(){
        this.channels = new HashMap<>();
    }
    @Override
    public void saveChannel(Channel channel){
        channels.put(channel.getId(), channel);
    }
    @Override
    public Optional<Channel> findChannelById(UUID id){
        // nullable 한 Optional 로 감싸서 null-safety를 보장
        return Optional.ofNullable(channels.get(id));
    }
    @Override
    public Collection<Channel> getAllChannels(){
        return channels.values();
    }
    @Override
    public void deleteAllChannels(){
        channels.clear();
    }
    @Override
    public void deleteChannelById(UUID id){
        channels.remove(id);
    }
}
