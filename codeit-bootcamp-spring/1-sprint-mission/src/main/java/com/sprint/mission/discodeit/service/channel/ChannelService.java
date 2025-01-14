package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.entity.channel.dto.ChangeChannelNameRequest;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;

public interface ChannelService {
    void createChannelOrThrow(CreateNewChannelRequest request);

    void changeChannelNameOrThrow(ChangeChannelNameRequest request);

    void deleteUserParticipatedChannelByChannelId(String channelId);
}
