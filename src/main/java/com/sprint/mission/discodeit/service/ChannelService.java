package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.HashMap;
import java.util.UUID;

public interface ChannelService {
    // 채널 추가
    void addChannel(Channel channel);

    // 단일 채널 조회
    Channel getChannel(UUID id);

    // 모든 채널 조회
    public HashMap<UUID, Channel> getAllChannels();

    // 채널 수정
    void updateChannel(UUID id, String name );

    // 채널 삭제
    void deleteChannel(UUID id);
}