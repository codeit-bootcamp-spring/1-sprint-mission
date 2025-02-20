package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.form.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.form.PublicChannelDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createPublicChannel(PublicChannelDto channel,UUID userId);
    void createPrivateChannel(PrivateChannelDto channel,UUID userId);
    Optional<PrivateChannelDto> findPrivateChannel(UUID id);
    Optional<PublicChannelDto> findPublicChannel(UUID id);
    List<Channel> findAllChannels();
    List<UUID> findAllByUserId(UUID userId);
    void updateChannel(UUID id, ChannelUpdateDto channelParam);
    void deleteChannel(UUID id);
}
