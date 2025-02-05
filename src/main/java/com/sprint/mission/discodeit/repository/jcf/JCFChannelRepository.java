package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.List;

public class JCFChannelRepository implements ChannelRepository {

    private final HashMap<String, Channel> data;

    //싱글톤으로 수정하기
    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public boolean delete(Channel channel) {
        return data.remove(channel.getId())!=null;
    }

    @Override
    public Channel findById(String id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

}
