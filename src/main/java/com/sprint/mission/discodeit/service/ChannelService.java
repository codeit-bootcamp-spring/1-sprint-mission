package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    public void Creat(Channel channel);

    public void Delete(Channel channel);

    public void Update(Channel channel, Channel updateChannel);

    public List<Channel> Write(UUID id);

    public List<Channel> AllWrite();
}
