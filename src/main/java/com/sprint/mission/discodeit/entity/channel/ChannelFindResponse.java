package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ChannelFindResponse {
    private final UUID channelId;
    private String channelName;
    private User owner;
    private String channelType;
    private ReadStatus readStatus;
}
