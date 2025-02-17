package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.form.ChannelUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ChannelRepository {
    Channel createChannel(UUID id, Channel channel);
    void updateChannel(UUID id, ChannelUpdateDto channelUpdateDto);
    void deleteChannel(UUID id);
    Optional<Channel> findById(UUID id);
    Optional<Channel> findByChannelName(String channelName);
    List<Channel> findAll();
}
