package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindAllByUserIdDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelServiceUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;


import java.util.List;
import java.util.UUID;

public interface ChannelService {


    //서비스 로직
    UUID createPublic(String name, String description);
    UUID createPrivate(UUID userId);
    ChannelServiceFindDTO find(UUID id);
    List<ChannelServiceFindDTO> findAll();
    List<ChannelServiceFindAllByUserIdDTO> findAllByUserId(UUID userId);
    Channel update(ChannelServiceUpdateDTO channelServiceUpdateDTO);
    UUID deleteChannel(UUID id);
}

