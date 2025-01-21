package com.srint.mission.discodeit.service;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;


import java.util.List;
import java.util.UUID;

public interface ChannelService {

    //db 로직
    UUID save(Channel channel);
    Channel findOne(UUID id);
    List<Channel> findAll();
    UUID delete (UUID id);

    //서비스 로직
    UUID create(String channelName, User channelOwner);
    Channel read(UUID id);
    List<Channel> readAll();
    Channel updateChannelName(UUID id, User user, String channelName);

    Channel joinChannel(UUID id, User user);

    UUID exitChannel(UUID id, User user);
    UUID deleteChannel(UUID id, User user);
}
