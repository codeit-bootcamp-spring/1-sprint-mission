package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createChannel(Channel channel);
    Optional<Channel> getChannel(UUID id);
    List<Channel> getAllChannels();
    void updateChannel(Channel channel);
    void deleteChannel(UUID id);
}