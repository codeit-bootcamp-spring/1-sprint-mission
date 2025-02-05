package com.srint.mission.discodeit.repository.jcf;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        data = new HashMap<>();
    }

    public UUID save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel.getId();
    }

    public Channel findOne(UUID id) {
        return data.get(id);
    }

    public List<Channel> findAll() {
/*        if(data.isEmpty()){
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
        return new ArrayList<>(data.values());
    }

    public UUID update(Channel channel){
        data.put(channel.getId(), channel);
        return channel.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }
}
