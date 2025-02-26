package com.sprint.mission.discodeit.dto.request.binary;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(name = "BinaryContentCreateDTO", description = "사용자와 메시지에 연관된 바이너리 콘텐츠 생성 요청 정보를 담은 DTO")
public record BinaryContentCreateDTO(
    @Schema(description = "사용자 식별자", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User Id is required") UUID userId,

    @Schema(description = "메시지 식별자", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Message Id is required") UUID messageId,

    @Schema(description = "파일 이름", example = "document.pdf")
    @NotEmpty(message = "Filename is required") String filename,

    @Schema(description = "파일의 콘텐츠 타입", example = "application/pdf")
    @NotEmpty(message = "Content type is required") String contentType,

    @Schema(description = "파일 데이터를 담은 byte 배열")
    byte[] bytes
) {

}
