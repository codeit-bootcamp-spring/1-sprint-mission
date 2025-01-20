package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.entity.channel.dto.ChangeChannelNameRequest;
import com.sprint.mission.discodeit.entity.channel.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.entity.channel.dto.DeleteChannelRequest;

public interface ChannelService {
    ChannelResponse createChannelOrThrow(CreateNewChannelRequest request);

    void changeChannelNameOrThrow(ChangeChannelNameRequest request);

    void deleteChannelByChannelIdOrThrow(DeleteChannelRequest request);
}
