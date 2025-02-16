package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(String channelName, ChannelType type);
    Channel save(ChannelType type);
    Channel findById(UUID id);
    List<Channel> findAll();
    boolean delete(UUID channelId);
    void update(UUID id, String name);
    void addMessage(UUID channelId, UUID messageId);
    List<UUID> findMessagesByChannelId(UUID channelId);
    void deleteMessageInChannel(UUID channelId, UUID messageId);
}
