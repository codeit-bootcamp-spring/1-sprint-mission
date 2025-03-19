package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    List<UUID> participantIds
) {

}
