package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import java.util.HashMap;

public interface ChannelRepository {
     Channel getChannel(UUID channelId);
     HashMap<UUID, Channel> getChannelsMap();
     boolean deleteChannel(UUID channelId);
     boolean addChannel(Channel channel);
     boolean isChannelExist(UUID channelId);
     boolean addChannelMember(UUID channelId, UUID memberId);
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
