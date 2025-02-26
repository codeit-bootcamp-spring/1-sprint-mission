package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PublicChannelCreateDto publicChannelCreateDto); //채널 생성
    Channel create(PrivateChannelCreateDto privateChannelCreateDto); //채널 생성
    ChannelDto findById(UUID channelId); // 채널 단일 조회
    List<ChannelDto> findAllByUserId(UUID userId); // 모든 채널 조회
    Channel update(UUID channelId, PublicChannelUpdateDto publicChannelUpdateDto); // 채널 수정
    void deleteById(UUID channelId); // 채널 삭제
}
