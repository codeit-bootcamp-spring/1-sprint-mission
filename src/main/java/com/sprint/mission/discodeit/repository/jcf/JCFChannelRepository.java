package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<Long, Channel> data;
    private final AtomicLong idGenerator = new AtomicLong(1);   // ID 초기값 1

    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long saveChannel(Channel channel) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, channel);
        return id;
    }

    @Override
    public Channel loadChannel(Long id) {
        return data.get(id);
    }

    @Override
    public Map<Long, Channel> loadAllChannels() {
        return data;
    }

    @Override
    public void deleteChannel(Long id) {
        if (data.remove(id) == null) {
            throw new IllegalArgumentException("삭제할 채널이 존재하지 않습니다.");
        }
    }

    @Override
    public void updateChannel(Long id, Channel channel) {
        data.put(id, channel);
    }
}
