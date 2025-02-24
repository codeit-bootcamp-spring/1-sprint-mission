package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDTO;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // 채널 생성
    ChannelDTO.idResponse createPublicChannel(ChannelDTO.request channelReqDTO);

    ChannelDTO.idResponse createPrivateChannel(ChannelDTO.request channelReqDTO);

    // 채널 조회
    // 특정 채널(단건)
    ChannelDTO.response find(Long id);

    ChannelDTO.response find(UUID uuid);

    // 모든 채널
    List<ChannelDTO.response> findAllByUserId(UUID userId);

    // 채널 수정
    ChannelDTO.idResponse update(ChannelDTO.update updateDTO);

    // 채널 삭제
    // 채널 Status 변경 -> inactive로
    // 채널 Hard Delete -> 진짜진짜삭제
    ChannelDTO.idResponse delete(Long id);

    ChannelDTO.idResponse delete(UUID uuid);
}