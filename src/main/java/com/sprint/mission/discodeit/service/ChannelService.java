package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelService extends CRUDService<Channel>{
    void channelOwnerChange(UUID id, User owner);
    void channelMemberJoin(UUID id, User user);
    void channelMemberWithdrawal(UUID id, User user);
}
