package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelRequest(
    List<UUID> participantIds   // 참가자 id
) {

}
