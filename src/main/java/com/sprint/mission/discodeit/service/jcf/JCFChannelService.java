package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.validation.InvalidChannelNameException;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.*;

public class JCFChannelService implements BaseService<Channel> {
    private final Map<UUID, Channel> data = new HashMap<>();

    private static void validChannel(Channel channel) {
        if (channel.getName() == null || channel.getName().isEmpty()) {
            throw new InvalidChannelNameException("유효하지 않은 채널명입니다.");
        }
    }

    @Override
    public Channel create(Channel channel) {
        validChannel(channel);

        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel readById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, Channel channel) {
        Channel checkChannel = data.get(id);
        validChannel(channel);
        checkChannel.update(channel.getName());
        return checkChannel;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
