package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdate;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannelPublic(PublicChannelCreate publicChannelCreate);
    Channel createChannelPrivate(PrivateChannelCreate privateChannelCreate);
    ChannelDto findById(UUID channelId);
    List<ChannelDto> findAllByUserID(UUID userId);
    Channel update(UUID channelId, PublicChannelUpdate publicChannelUpdate);
    void delete(UUID channelId);
}
