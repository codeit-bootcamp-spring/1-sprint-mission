package com.sprint.mission.discodeit.event;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PrivateChannelListChangedEvent {

    private final List<UUID> userIds;
    private final UUID channelId;
}
