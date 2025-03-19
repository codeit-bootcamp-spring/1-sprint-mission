package com.spirnt.mission.discodeit.dto.readStatus;

import com.spirnt.mission.discodeit.enity.ReadStatus;
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

  @Builder
  public ReadStatusDto(UUID id, UUID userId, UUID channelId, Instant lastReadAt) {
    this.id = id;
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = lastReadAt;
  }

  public static ReadStatusDto from(ReadStatus readStatus) {
    return ReadStatusDto.builder()
        .id(readStatus.getId())
        .userId(readStatus.getUser().getId())
        .channelId(readStatus.getChannel().getId())
        .lastReadAt(readStatus.getLastReadAt())
        .build();
  }
}
