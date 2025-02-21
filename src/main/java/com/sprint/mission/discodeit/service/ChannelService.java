package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.channel.*;

import java.util.Collection;
import java.util.UUID;

public interface ChannelService {
    ChannelPublicResponse createPublicChannel(ChannelPublicRequest channelPublicRequest);
    ChannelPrivateResponse createPrivateChannel(ChannelPrivateRequest channelPrivateRequest);

    // Read : 전체 채널 조회, 특정 채널 조회
    Collection<ChannelFindAllResponse> findAllByUserId(UUID userId);
    ChannelFindResponse getChannelById(UUID id);

    // Update : 특정 채널 이름 변경
    void updateChannel(UUID id, ChannelUpdateRequest channelUpdateRequest);

    // Delete : 특정 채널 삭제
    void deleteChannelById(UUID id);
}
