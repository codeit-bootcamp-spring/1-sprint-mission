package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record CreateChannelRequest(String channelName, UUID userId) {

}
