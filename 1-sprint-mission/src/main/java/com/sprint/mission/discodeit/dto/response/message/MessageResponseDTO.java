package com.sprint.mission.discodeit.dto.response.message;

import com.sprint.mission.discodeit.dto.response.binary.BinaryContentDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(name = "MessageResponseDTO", description = "메시지 응답 정보를 담은 DTO")
public record MessageResponseDTO(
    @Schema(description = "메시지 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Message Id is required") UUID messageId,

    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User Id is required") UUID userId,

    @Schema(description = "채널 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Channel Id is required") UUID channelId,

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    @NotEmpty(message = "Content is required") String content,

    @Schema(description = "첨부 파일 목록")
    List<BinaryContentDTO> attachments,

    @Schema(description = "메시지 생성 시간", example = "2021-09-01T12:00:00Z")
    Instant createdAt
) {

}
