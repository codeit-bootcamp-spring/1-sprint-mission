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
        if (data.containsKey(channel.getId())) {
            throw new IllegalArgumentException("이미 존재하는 채널입니다: " + channel.getName());
        }
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
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("해당 ID의 채널을 찾을 수 없습니다: " + id);
        }
        data.put(id, channel);
    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("삭제할 채널이 존재하지 않습니다: " + id);
        }
        data.remove(id);
    }
}
