package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    // 저장
    Channel saveChannel(Channel channel);
    // 읽기
    Optional<Channel> findChannelById(UUID id);
    Collection<Channel> getAllChannels();
    // 삭제
    void deleteChannelById(UUID id);

}
