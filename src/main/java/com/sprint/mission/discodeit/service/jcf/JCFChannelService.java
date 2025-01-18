package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public void addChannel(Channel channel) {
        // 채널 이름 중복 검사
        boolean isDuplicateName = data.values().stream()
                .anyMatch(existingChannel -> existingChannel.getName().equals(channel.getName()));
        if (isDuplicateName) {
            throw new IllegalArgumentException("채널 이름이 이미 존재합니다: " + channel.getName());
        }
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel getChannel(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, String newName) {
        Channel channel = data.get(id);
        if (channel == null) {
            throw new NoSuchElementException("존재하지 않는 채널 ID: " + id);
        }

        // 새 이름 중복 검사
        boolean isDuplicateName = data.values().stream()
                .anyMatch(existingChannel -> existingChannel.getName().equals(newName));
        if (isDuplicateName) {
            throw new IllegalArgumentException("채널 이름이 이미 존재합니다: " + newName);
        }
        channel.updateName(newName);
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
