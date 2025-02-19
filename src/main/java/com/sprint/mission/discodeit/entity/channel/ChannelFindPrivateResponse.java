package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;

import java.util.List;
import java.util.UUID;

public class ChannelFindPrivateResponse extends ChannelFindResponse {
    private List<UUID> userIdList;

    public ChannelFindPrivateResponse(UUID channelId, String channelName, User owner, String channelType, ReadStatus readStatus, List<UUID> userIdList) {
        super(channelId, channelName, owner, channelType, readStatus);
        this.userIdList = userIdList;
    }
}
