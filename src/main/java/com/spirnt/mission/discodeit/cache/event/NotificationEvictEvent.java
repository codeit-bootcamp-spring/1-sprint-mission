package com.spirnt.mission.discodeit.cache.event;

import java.util.List;
import java.util.UUID;

public record NotificationEvictEvent(
    List<UUID> userIds
) {

}
