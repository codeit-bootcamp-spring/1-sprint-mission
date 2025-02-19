package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
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
    public void delete(UUID channelId) {
        data.remove(channelId);
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}

