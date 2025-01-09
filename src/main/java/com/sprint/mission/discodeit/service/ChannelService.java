package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel create(Channel channel);
    Optional<Channel> read(Channel channel);
    List<Channel> readAll();
    Channel update(Channel existChannel, Channel updateChannel);
    boolean delete(Channel channel);
}
