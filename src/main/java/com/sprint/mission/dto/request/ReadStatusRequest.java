package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.ReadStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ReadStatusRequest {

    private UUID channelId;
    private UUID userId;
    private Instant lastReadAt;

    public ReadStatus toEntity() {
        return new ReadStatus(channelId, userId, lastReadAt);
    }
}
