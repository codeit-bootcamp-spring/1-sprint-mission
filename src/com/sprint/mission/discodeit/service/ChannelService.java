package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface ChannelService {


    void createChannel(Channel channel);

    Channel readChannel(String id);

    List<Channel> readAllChannel();
    public void modifyChannel (String id);
    public void deleteChannel (String id);
}
