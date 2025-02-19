package com.sprint.mission.discodeit.application.service.interfaces;

import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelNameRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelSubjectRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.CreateChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.DeleteChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.FoundChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.InviteChannelRequestDto;
import com.sprint.mission.discodeit.domain.channel.Channel;
import java.util.UUID;

public interface ChannelService {

    ChannelCreateResponseDto createPublicChannel(UUID userId, CreateChannelRequestDto requestDto);

    ChannelCreateResponseDto createPrivateChannel(UUID userId, CreateChannelRequestDto requestDto);

    void joinChannel(UUID invitedUserId, InviteChannelRequestDto requestDto);

    FoundChannelResponseDto findOneByChannelId(UUID channelId);

    Channel findOneByChannelIdOrThrow(UUID uuid);

    void changeSubject(UUID userId, ChangeChannelSubjectRequestDto requestDto);

    void changeChannelName(UUID userId, ChangeChannelNameRequestDto requestDto);

    void deleteChannel(UUID userId, DeleteChannelRequestDto requestDto);
}
