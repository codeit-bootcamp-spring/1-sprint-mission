package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelService extends CRUDService<Channel>{
    Channel channelOwnerChange(UUID id, User owner);
    boolean channelMemberJoin(UUID id, User user);
    boolean channelMemberWithdrawal(UUID id, User user);
}
