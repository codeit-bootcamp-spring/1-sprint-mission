package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel); // 채널 저장
    Channel findById(UUID id); // ID로 채널 찾기
    List<Channel> findAll(); // 모든 채널 찾기
    void update(UUID id, Channel channel); // 채널 업데이트
    void delete(UUID id); // 채널 삭제
}
