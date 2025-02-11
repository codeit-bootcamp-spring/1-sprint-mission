package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createPublicChannel(ChannelCreateDTO channelCreateDTO);
    void createPrivateChannel(UUID creatorId, List<UUID> members); // ✅ 추가된 메서드
    List<ChannelDTO> readAll();
    Optional<ChannelDTO> read(UUID channelId);
    void delete(UUID channelId);
}
