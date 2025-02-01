package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelRepository {
    // 저장
    boolean saveChannel(Channel channel);

    // 조회
    Channel loadChannel(Channel channel);
    List<Channel> loadAllChannels();

    // 삭제
    boolean deleteChannel(Channel channel);
}
