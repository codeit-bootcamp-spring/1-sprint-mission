package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    // 저장
    boolean saveChannel(Channel channel);

    // 조회
    Channel loadChannel(UUID uuid);
    List<Channel> loadAllChannels();

    // 삭제
    boolean deleteChannel(Channel channel);
}
