package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelFindAllByUserIdDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;


import java.util.List;
import java.util.UUID;

public interface ChannelService {


    //서비스 로직
    UUID createPublic(String name, String description);
    UUID createPrivate(UUID userId);
    ChannelFindDTO find(UUID id);
    List<ChannelFindDTO> findAllByUserId(UUID userId);
    Channel update(UUID id, ChannelUpdateDTO dto);
    UUID delete(UUID id);
}

