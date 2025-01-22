package com.srint.mission.discodeit.repository.jcf;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        data = new HashMap<>();
    }

    //db 로직
    @Override
    public UUID save(Channel channel) {
        data.put(channel.getId(), channel);
        return(channel.getId());
    }

    @Override
    public Channel findOne(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("조회할 Channel을 찾지 못했습니다.");
        }
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        if(data.isEmpty()){
            throw new IllegalArgumentException("Channel이 없습니다.");
        }
        return data.values().stream().toList();
    }

    @Override
    public UUID delete(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("삭제할 Channel을 찾지 못했습니다.");
        }
        data.remove(id);
        return id;
    }
}
