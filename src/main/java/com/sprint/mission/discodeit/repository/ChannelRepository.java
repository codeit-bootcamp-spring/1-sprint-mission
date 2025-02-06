package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;

public interface ChannelRepository {
    public Channel getChannel(UUID channelId);
    public HashMap<UUID, Channel> getChannelsMap();
    public boolean deleteChannel(UUID channelId);
    public boolean addChannel(Channel channel);
    public boolean isChannelExist(UUID channelId);
    public boolean addChannelMember(UUID channelId, User member);
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
