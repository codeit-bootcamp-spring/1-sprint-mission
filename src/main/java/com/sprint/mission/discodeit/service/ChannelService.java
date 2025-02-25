package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    ChannelDTO createChannel(ChannelCreateDTO channelCreateDTO);
    List<ChannelDTO> readAll();
    Optional<ChannelDTO> read(UUID channelId);
    void update(UUID channelId, ChannelUpdateDTO channelUpdateDTO);
    void delete(UUID channelId);
    List<ChannelDTO> getChannelsForUser(UUID userId);
}
