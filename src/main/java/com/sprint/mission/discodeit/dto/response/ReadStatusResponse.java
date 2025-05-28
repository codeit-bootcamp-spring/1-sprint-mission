package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusResponse(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

//  public static ReadStatusResponse entityToDto(ReadStatus readStatus) {
//    return ReadStatusResponse.builder()
//        .id(readStatus.getId())
//        .userId(readStatus.getUser().getId())
//        .channelId(readStatus.getChannel().getId())
//        .lastReadAt(readStatus.getLastReadAt())
//        .build();
//  }
}
