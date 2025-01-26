package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelRepository extends BaseRepository<Channel>{
    Channel ownerChange(UUID id, User Owner);
    boolean memberJoin(UUID id, User user);
    boolean memberWithdrawal(UUID id, User user);
}
