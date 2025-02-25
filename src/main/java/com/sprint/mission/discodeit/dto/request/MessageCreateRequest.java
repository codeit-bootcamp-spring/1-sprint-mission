package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Message 생성 정보")
public record MessageCreateRequest(
    @Schema(description = "메시지 내용", example = "안녕하세요!")
    String content,

    @Schema(description = "채널 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID channelId,

    @Schema(description = "작성자 ID", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID authorId
) {

}