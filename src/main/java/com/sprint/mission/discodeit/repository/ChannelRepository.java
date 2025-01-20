package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);  // 채널 저장

    Optional<Channel> findById(UUID id);  // 특정 채널 조회

    List<Channel> findAll();  // 모든 채널 조회

    void update(UUID id, Channel channel);  // 채널 수정

    void delete(UUID id);  // 채널 삭제
}
