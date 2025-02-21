package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PublicChannelCreateRequest publicChannelCreateRequest);
    Channel create(PrivateChannelCreateRequest privateChannelCreateRequest);
    ChannelResponseDto find(UUID channelId);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    Instant findLastMessageTime(UUID channelId);
    List<UUID> findParticipantsIds(Channel channel);
    ChannelResponseDto getChannelInfo(Channel channel, Instant lastMessageTime, List<UUID> participantsIds);
    ChannelResponseDto update(PublicChannelUpdateRequest channelUpdateRequestDto);
    void delete(UUID channelId);
}
