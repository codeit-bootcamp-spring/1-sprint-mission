package com.sprint.mission.discodeit.service;



import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    void createChannel(Channel channel);
    Channel readChannel(String channelUuid);
    List<Channel> readAllChannels();
    void updateChannel(String channelUuid, String newChannelTitle);
    void deleteChannel(String channelUuid);
}
