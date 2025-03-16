package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(
    UUID id,
    Instant createdAt, // TODO찐: date-time으로 수정
    Instant updatedAt, // TODO찐: date-time으로 수정
    String content,
    UUID channelId,
    UserDto author, // // TODO찐: Object 형으로 수정
    java.util.List<BinaryContentDto> attachments // TODO찐: array<Object> 형으로 수정
) {

}
