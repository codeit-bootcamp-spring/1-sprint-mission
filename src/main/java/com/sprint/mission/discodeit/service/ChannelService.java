package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelService {
    public abstract UUID createChannel(User user, String channelName);

    // Read : 전체 채널 조회, 특정 채널 조회
    public abstract int showAllChannels();
    public abstract Channel getChannelById(UUID id);

    // Update : 특정 채널 이름 변경
    public abstract void updateChannelName(UUID id);

    // Delete : 전체 채널 삭제, 특정 채널 삭제
    public abstract void deleteAllChannels();
    public abstract void deleteChannelById(UUID id);
}
