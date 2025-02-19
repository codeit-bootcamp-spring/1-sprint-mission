package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;


import java.util.List;
import java.util.UUID;

public interface ChannelService {


    //서비스 로직
    UUID create(ChannelCreatePublicDTO channelCreatePublicDTO);
    UUID create(ChannelCreatePrivateDTO dto);
    ChannelFindDTO find(UUID id);
    List<ChannelFindDTO> findAllByUserId(UUID userId);
    Channel update(UUID id, ChannelUpdateDTO dto);
    UUID delete(UUID id);
}

