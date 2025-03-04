package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(name = "MessageUpdateDTO", description = "메시지 업데이트 요청 정보를 담은 DTO")
public record MessageUpdateDTO(
    @Schema(description = "메시지 Id", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Message Id is required") UUID messageId,

    @Schema(description = "새 메시지 내용", example = "수정된 메시지 내용")
    @NotBlank(message = "Content is required") String newContent,

    @Schema(description = "첨부파일 목록")
    List<BinaryContentCreateDTO> attachments
) {

}
