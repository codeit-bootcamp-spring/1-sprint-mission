package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {

    private static final JCFChannelRepository jcfChannelRepository = new JCFChannelRepository();
    private final Map<UUID, Channel> data;

    private JCFChannelRepository() {
        data = new HashMap<>(100);
    }

    public static JCFChannelRepository getInstance() {
        return jcfChannelRepository;
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel입니다."));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(c -> {
                    try {
                        return c.getType() == ChannelType.PUBLIC || c.getUser(userId) != null;
                    } catch (NotFoundException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        data.remove(channelId);
    }
}
