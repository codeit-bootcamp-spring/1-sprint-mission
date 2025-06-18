package com.spirnt.mission.discodeit.dto.readStatus;

import com.spirnt.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadStatusDto {

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
    private boolean notificationEnabled;

    @Builder
    public ReadStatusDto(UUID id, UUID userId, UUID channelId, Instant lastReadAt,
        boolean notificationEnabled) {
        this.id = id;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
        this.notificationEnabled = notificationEnabled;
    }

    public static ReadStatusDto from(ReadStatus readStatus) {
        return ReadStatusDto.builder()
            .id(readStatus.getId())
            .userId(readStatus.getUser().getId())
            .channelId(readStatus.getChannel().getId())
            .lastReadAt(readStatus.getLastReadAt())
            .notificationEnabled(readStatus.isNotificationEnabled())
            .build();
    }
}
