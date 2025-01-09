package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ChannelEntity;

import java.util.UUID;

public interface ChannelService {
    // 채널 추가
    void addChannel(ChannelEntity channel);
    // 채널 호출
    ChannelEntity getChannelById(UUID id);
    // 채널 호출 (DelFlg 검증)
    ChannelEntity getChannelByIdWithFlg(UUID id);
    // 채널명 갱신
    void updateChannelName(UUID id, String channelName);
    // 채널설명 갱신
    void updateChannelDescription(UUID id, String description);
    // 채널 삭제
    void deleteChannel(UUID id);
    // 채널 삭제(Flg Update 방식)
    void deleteChannelWithFlg(UUID id);
}
