package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createPublicChannel(ChannelCreateDTO channelCreateDTO);
    void createPrivateChannel(UUID creatorId, List<UUID> members);
    List<ChannelDTO> readAll();
    Optional<ChannelDTO> read(UUID channelId);
    void update(UUID channelId, ChannelUpdateDTO channelUpdateDTO);
    void delete(UUID channelId);

    // ✅ 특정 사용자가 볼 수 있는 채널 목록 조회 추가
    List<ChannelDTO> getChannelsForUser(UUID userId);
}
