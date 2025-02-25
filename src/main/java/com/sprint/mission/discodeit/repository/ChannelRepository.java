package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    void deleteById(UUID id);

    // ✅ 특정 사용자가 볼 수 있는 비공개 채널 목록 조회 추가
    List<Channel> findAllPrivateChannelsByUserId(UUID userId);
}
