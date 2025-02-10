package com.sprint.mission.discodeit.application.service.interfaces;

import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelNameRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChangeChannelSubjectRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.application.dto.channel.CreateChannelRequestDto;
import com.sprint.mission.discodeit.application.dto.channel.DeleteChannelRequestDto;
import com.sprint.mission.discodeit.domain.channel.Channel;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto create(UUID userId, CreateChannelRequestDto requestDto);

    Channel findOneByIdOrThrow(UUID uuid);

    void changeSubject(UUID userId, ChangeChannelSubjectRequestDto requestDto);

    void changeChannelName(UUID userId, ChangeChannelNameRequestDto requestDto);

    void deleteChannel(UUID userId, DeleteChannelRequestDto requestDto);
}
