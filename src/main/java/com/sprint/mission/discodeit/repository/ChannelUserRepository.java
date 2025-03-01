package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ChannelUser;

import java.util.List;
import java.util.UUID;

public interface ChannelUserRepository {
    ChannelUser save(ChannelUser channelUser);

    List<UUID> findUserByChannelId(UUID channelId);
    List<UUID> findChannelByUserId(UUID userId);

    // 채널 삭제, 유저 삭제의 경우 상정
    void deleteByChannelId(UUID channelId);
    void deleteByUserId(UUID userId);
}
