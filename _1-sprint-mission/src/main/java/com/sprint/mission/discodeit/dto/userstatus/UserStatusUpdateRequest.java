package com.sprint.mission.discodeit.dto.userstatus;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "변경할 User 온라인 상태 정보")
public record UserStatusUpdateRequest(
    Instant newLastConnectTime
) {

}
