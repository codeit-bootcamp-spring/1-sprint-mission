package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;


import java.util.List;
import java.util.UUID;

public interface ChannelService {


    //서비스 로직
    UUID create(String name, String description, ChannelType type);
    Channel read(UUID id);
    List<Channel> readAll();
    Channel updateChannel(UUID id, String name, String description, ChannelType type);
    UUID deleteChannel(UUID id);
}

