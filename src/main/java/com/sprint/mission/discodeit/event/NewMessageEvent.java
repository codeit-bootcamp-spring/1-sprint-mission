package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NewMessageEvent {

    private final UUID authorId;
    private final String authorName;
    private final UUID channelId;
    private final String channelName;
    private final ChannelType channelType;
    private final String content;
    private final List<UUID> receiverIds;
}
