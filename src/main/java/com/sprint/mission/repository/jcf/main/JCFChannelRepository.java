package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.service.exception.NotFoundId;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        // optional no
        return data.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID id) {
        // null 예외처리 여기서 확실히 잡기 : 다른 메서드가 findById 활용을 많이 함
        Channel findChannel = data.get(id);
        if (findChannel == null) throw new NotFoundId();
        else return findChannel;
    }

    @Override
    public Set<Channel> findAll() {
        return new HashSet<>(data.values());
    }

    @Override
    public void delete(Channel deletingChannel) {

        System.out.printf("채널명 %s는 사라집니다.", deletingChannel.getName());
        data.remove(deletingChannel.getId());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}

