package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private static JCFChannelService instance; // 싱글톤 인스턴스
    private final Map<UUID, Channel> data = new HashMap<>();

    private JCFChannelService() {} // private 생성자

    public static JCFChannelService getInstance() {
        if (instance == null) {
            synchronized (JCFChannelService.class) {
                if (instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

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
}
