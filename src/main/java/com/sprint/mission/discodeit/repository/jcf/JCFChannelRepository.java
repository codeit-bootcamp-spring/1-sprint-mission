package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    Map<UUID, Channel> channels;
    public JCFChannelRepository(){
        this.channels = new HashMap<>();
    }
    public void saveChannel(Channel channel){
        channels.put(channel.getId(), channel);
    }
    public Optional<Channel> findChannelById(UUID id){
        // nullable 한 Optional 로 감싸서 null-safety를 보장
        return Optional.ofNullable(channels.get(id));
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
