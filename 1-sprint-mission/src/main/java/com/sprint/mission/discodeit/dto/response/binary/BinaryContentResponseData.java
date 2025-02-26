package com.sprint.mission.discodeit.dto.response.binary;

import com.sprint.mission.discodeit.entity.BinaryContent;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "BinaryContentResponseData", description = "파일 바이너리 콘텐츠의 상세 응답 정보를 담은 DTO")
public record BinaryContentResponseData(
    @Schema(description = "바이너리 콘텐츠 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "BinaryContentId is required") UUID binaryContentId,

    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User Id is required") UUID userId,

    @Schema(description = "파일 이름", example = "image.png")
    @NotBlank(message = "Filename is required") String filename,

    @Schema(description = "파일의 콘텐츠 타입", example = "image/png")
    @NotBlank(message = "Filename is required") String contentType,

    @Schema(description = "파일 생성 시간", example = "2021-09-01T12:00:00Z")
    Instant createdAt
) {

  public static BinaryContentResponseData fromEntity(BinaryContent binaryContent) {
    return new BinaryContentResponseData(
        binaryContent.getId(),
        binaryContent.getUserId(),
        binaryContent.getFilename(),
        binaryContent.getContentType(),
        binaryContent.getCreatedAt()
    );
  }
}
