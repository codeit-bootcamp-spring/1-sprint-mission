package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    public void creat(Channel channel);

    public void delete(UUID channelId);

    public void update(UUID channelId, String title);

    public UUID write(String title);

    public List<Channel> allWrite();


}
