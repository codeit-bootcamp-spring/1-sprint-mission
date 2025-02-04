package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    //채널에 대한 CURD.

    Channel createChannel(String name);

    Channel readChannel(UUID id);

    List<Channel> readAllChannel();
    Channel modifyChannel(UUID id, String name); //채널 네임 변경
    void deleteChannel (UUID id);

}
