package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;

public record UserUserStatusUpdateDTO(
        //굳이 있어야하나? userStatus패키지의 updateDTO 이용해도 무방?
        Instant time
) {
}
