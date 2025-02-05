package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.Collection;
import java.util.UUID;

public interface ChannelService {
    UUID createChannel(User user, String channelName);

    // Read : 전체 채널 조회, 특정 채널 조회
    Collection<Channel> showAllChannels();
    Channel getChannelById(UUID id);

    // Update : 특정 채널 이름 변경
    void updateChannelName(UUID id);

    // Delete : 전체 채널 삭제, 특정 채널 삭제
    void deleteAllChannels();
    void deleteChannelById(UUID id);
}
