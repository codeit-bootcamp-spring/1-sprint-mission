package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel newChannel);

    Optional<Channel> getChannelById(UUID uuid);

    List<UUID> getMessagesUUIDFromChannel(UUID uuid);

    Optional<Channel> addMessageToChannel(UUID channelUUID, UUID messageUUID);

    Optional<Channel> updateChannelName(UUID uuid, String channelName);

    void deleteChannel(UUID uuid);

    List<Channel> getAllChannels();

    void save();

    boolean existsById(UUID uuid);
}
