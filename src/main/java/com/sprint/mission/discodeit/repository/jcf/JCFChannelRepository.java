package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<String, Channel> data = new HashMap<>();

    @Override
    public void saveAll(List<Channel> channels) {
        channels.forEach(channel -> data.put(channel.getId().toString(), channel));
    }

    @Override
    public List<Channel> loadAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void reset() {
        data.clear(); // 메모리 데이터 초기화
        System.out.println("메모리 데이터가 초기화되었습니다.");
    }
}
