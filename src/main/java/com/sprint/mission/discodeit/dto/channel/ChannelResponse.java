package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(UUID channelId, String channelName, Boolean isPrivate,
                              Instant lastMessageTime, List<UserResponse> userList) {

}
