package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    public void creat(Channel channel);

    public void delete(Channel channel);

    public void update(Channel channel, String title);

    public Channel write(String title);

    public List<Channel> allWrite();


}
