package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override // 나중에 void로
    public Channel save(Channel channel) {
        // optional no
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(Channel deletingChannel) {
        log.info("채널명 {}(은)는 사라집니다.", deletingChannel.getName());
        data.remove(deletingChannel.getId());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}

