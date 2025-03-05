package com.sprint.mission.discodeit.entity.status.user;

import java.time.Instant;

public record UserStatusReponse (
        String userName,
        String state,
        Instant lastAccessTime
){
}
