package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void create(Channel channel); //채널 생성
    Channel read(UUID id); // 채널 단일 조회
    List<Channel> readAll(); // 모든 채널 조회
    void update(UUID id,Channel channel); // 채널 수정
    void delete(UUID id); // 채널 삭제
}
