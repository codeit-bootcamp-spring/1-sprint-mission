package com.sprint.mission.discodeit.event;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PublicChannelListChangedEvent {

    private final UUID channelId;
}
