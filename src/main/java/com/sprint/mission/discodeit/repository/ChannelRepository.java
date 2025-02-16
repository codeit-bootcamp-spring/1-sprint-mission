package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.form.ChannelUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ChannelRepository {
    void createChannel(UUID id, Channel channel);
    void updateChannel(UUID id, ChannelUpdateDto channelUpdateDto);
    void deleteChannel(UUID id);
    Optional<Channel> findById(UUID id);
    Optional<Channel> findByChannelName(String channelName);
    List<Channel> findAll();
}
