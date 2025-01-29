package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;

public interface ChannelRepository {

    public Long saveChannel(Channel channel);

    public Channel loadChannel(Long id);

    public Map<Long, Channel> loadAllChannels();

    public void deleteChannel(Long id);

    public void updateChannel(Long id, Channel channel);
}
