package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Channel channel) {
        if (data.containsKey(id)) {
            data.put(id, channel);
        } else {
            throw new IllegalArgumentException("Channel not found for ID: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    // 추가: 데이터가 없을 때 예외를 발생시키는 메서드
    public Channel findByIdOrThrow(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("Channel not found for ID: " + id);
        }
        return data.get(id);
    }
}
