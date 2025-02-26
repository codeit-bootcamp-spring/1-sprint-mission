package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Participant;
import com.sprint.mission.discodeit.web.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.web.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.web.dto.PublicChannelDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(PublicChannelDto channel,Participant... participants);
    Channel createPrivateChannel(PrivateChannelDto channel,Participant... participants);
    Optional<?> findById(UUID id);
    List<PrivateChannelDto> findAllPrivateChannels();
    List<PublicChannelDto> findAllPublicChannels();
    List<UUID> findAllByLoginId(String loginId);
    void updateChannel(UUID id, ChannelUpdateDto channelParam);
    void deleteChannel(UUID id);
}
