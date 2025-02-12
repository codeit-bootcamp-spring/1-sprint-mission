package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelList;

    public JCFChannelRepository() {
        this.channelList = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        channelList.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public boolean isChannelExsit(UUID uuid) {
        Channel channel = channelList.get(uuid);
        if (channel == null) {
            return false;
        }
        return true;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = channelList.get(id);
        if (channel == null) {
            throw new IllegalArgumentException("해당 객체가 존재하지 않습니다.");
        }
        return channel;
    }

    @Override
    public Map<UUID, Channel> load() {
        if (channelList.isEmpty()) {
            throw new IllegalStateException("Channel 목록이 초기화되지 않았습니다.");
        }
        return channelList;
    }


    @Override
    public void delete(UUID id) {
        channelList.remove(id);
    }
}
