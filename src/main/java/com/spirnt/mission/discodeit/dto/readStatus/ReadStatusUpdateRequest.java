package com.spirnt.mission.discodeit.dto.readStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;


@Schema(description = "수정할 읽음 상태 정보")
public record ReadStatusUpdateRequest(
    Instant newLastReadAt,
    boolean newNotificationEnabled
) {

}
