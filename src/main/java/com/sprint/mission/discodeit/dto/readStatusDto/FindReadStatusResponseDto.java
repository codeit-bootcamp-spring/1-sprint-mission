package com.sprint.mission.discodeit.dto.readStatusDto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class FindReadStatusResponseDto {
    UUID id;
    UUID userId;
    UUID channelId;
    Instant lastReadTime;

    public static FindReadStatusResponseDto fromEntity(ReadStatus readStatus) {
        return new FindReadStatusResponseDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadTime()
        );
    }
}
