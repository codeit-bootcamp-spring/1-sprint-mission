package com.srint.mission.discodeit.service;

import com.srint.mission.discodeit.entity.Channel;


import java.util.List;
import java.util.UUID;

public interface ChannelService {

    UUID create(Channel channel);

    Channel read(UUID id);

    List<Channel> readAll();

    Channel update(UUID id, Channel channel);

    UUID delete(UUID id);
}
