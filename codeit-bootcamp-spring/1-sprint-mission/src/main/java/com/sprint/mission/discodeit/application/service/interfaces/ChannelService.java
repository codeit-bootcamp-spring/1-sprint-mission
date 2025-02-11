package com.sprint.mission.discodeit.application.service.interfaces;

import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelNameRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelSubjectRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.CreateChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.DeleteChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.InviteChannelRequestDto;
import com.sprint.mission.discodeit.domain.channel.Channel;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto createPublicChannel(UUID userId, CreateChannelRequestDto requestDto);

    ChannelResponseDto createPrivateChannel(UUID userId);

    void joinPublicChannel(UUID invitedUserId, InviteChannelRequestDto requestDto);

    void joinPrivateChannel(UUID invitedUserId, InviteChannelRequestDto requestDto);

    Channel findOneByIdOrThrow(UUID uuid);

    void changeSubject(UUID userId, ChangeChannelSubjectRequestDto requestDto);

    void changeChannelName(UUID userId, ChangeChannelNameRequestDto requestDto);

    void deleteChannel(UUID userId, DeleteChannelRequestDto requestDto);
}
