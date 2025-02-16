package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.form.PrivateChannelDto;
import com.sprint.mission.discodeit.entity.form.PublicChannelDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createPublicChannel(PublicChannelDto channel);
    void createPrivateChannel(PrivateChannelDto channel);
    Optional<PrivateChannelDto> findPrivateChannel(UUID id);
    Optional<PublicChannelDto> findPublicChannel(UUID id);
    List<Channel> findAllChannels();
//    List<PrivateChannelDto> findAllPrivateChannels(UUID userId);
    void updateChannel(UUID id, ChannelUpdateDto channelParam);
    void deleteChannel(UUID id);
}
