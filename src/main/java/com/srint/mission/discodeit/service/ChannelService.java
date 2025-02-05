package com.srint.mission.discodeit.service;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.ChannelType;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;


import java.util.List;
import java.util.UUID;

public interface ChannelService {

/*
    //db 로직
    UUID save(Channel channel);
    Channel findOne(UUID id);
    List<Channel> findAll();
    UUID update(Channel channel);
    UUID delete (UUID id);
*/

    //서비스 로직
    UUID create(String name, String description, ChannelType type);
    Channel read(UUID id);
    List<Channel> readAll();
    Channel updateName(UUID id, String name);
    Channel updateDescription(UUID id, String description);
    Channel updateType(UUID id, ChannelType type);
    UUID deleteChannel(UUID id);
}

