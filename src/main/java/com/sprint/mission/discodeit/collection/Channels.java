package com.sprint.mission.discodeit.collection;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.*;

public class Channels {
    private final Map<UUID, Channel> channels = new HashMap<>();

    // 채널 추가
    public void add(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    // 채널 단건 조회
    public Optional<Channel> get(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }

    // 채널 삭제
    public Optional<Channel> remove(UUID id) {
        return Optional.ofNullable(channels.remove(id));
    }

    // 채널 이름 업데이트
    public Optional<Channel> updateName(UUID id, String newName) {
        Channel channel = channels.get(id);
        if (channel != null) {
            channel.updateChannelName(newName);
            return Optional.of(channel);
        }
        return Optional.empty();
    }

    // 모든 채널 반환 (읽기 전용)
    public Map<UUID, Channel> asReadOnly() {
        return Collections.unmodifiableMap(channels);
    }

    // 채널 메시지 추가
    public Optional<Channel> addMessageToChannel(UUID channelId, UUID messageId) {
        Channel channel = channels.get(channelId);
        if (channel != null) {
            channel.addMessageToChannel(messageId);
            return Optional.of(channel);
        }
        return Optional.empty();
    }
}

