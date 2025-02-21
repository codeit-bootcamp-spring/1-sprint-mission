package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.ResponseChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel savePublic(PublicChannelDto publicChannelDto); //채널 생성
    Channel savePrivate(PrivateChannelDto privateChannelDto); //채널 생성
    ResponseChannelDto findById(UUID channelId); // 채널 단일 조회
    List<ResponseChannelDto> findAllByUserId(UUID userId); // 모든 채널 조회
    Channel update(ChannelUpdateDto channelUpdateDto); // 채널 수정
    void deleteById(UUID channelId); // 채널 삭제
}
