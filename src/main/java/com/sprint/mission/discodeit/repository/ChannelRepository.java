package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;
import java.util.HashMap;

public interface ChannelRepository {
     //todo add, remove 메서드가아니라 update메서드를 사용해서 유저리스트를 전달받아 업데이트하는 식으로 만드는게 나을까?
     Channel getChannel(UUID channelId) throws Exception;
     HashMap<UUID, Channel> getChannelsMap() throws Exception;
     boolean deleteChannel(UUID channelId) throws Exception;
     boolean saveChannel(Channel channel) throws Exception;
     boolean isChannelExist(UUID channelId) throws Exception;
     boolean addChannelMember(UUID channelId, UUID memberId) throws Exception;
     boolean removeChannelMember(UUID channelId, UUID memberId) throws Exception;
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
