package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelService {
    void createChannel(Channel channel);
    Channel readChannelInfo(UUID id);
    <T> void updateChannelField(UUID channelId, String fieldName, T contents);
    void deleteChannel(UUID idOfChannel);

}
