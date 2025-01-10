package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelService extends CRUDService<Channel>{
    void channelOwnerChange(UUID id, User owner);
    void channelMemberJoin(UUID id, User user);
}
