package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(name = "MessageCreateDTO", description = "새 메시지 생성 요청 정보를 담은 DTO")
public record MessageCreateDTO(
    @Schema(description = "보내는 사용자 Id", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User Id is required") UUID userId,

    @Schema(description = "메시지가 게시될 채널 Id", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Channel Id is required") UUID channelId,

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    @NotEmpty(message = "Content is required") String content,

    @Schema(description = "첨부파일 요청 DTO 목록")
    List<BinaryContentCreateRequest> attachments
) {

}
