package com.sprint.mission.discodeit.dto.response.binary;

import com.sprint.mission.discodeit.entity.BinaryContent;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(name = "BinaryContentDTO", description = "파일 바이너리 콘텐츠 응답 정보를 담은 DTO")
public record BinaryContentDTO(
    @Schema(description = "바이너리 콘텐츠 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "BinaryContentId is required") UUID binaryContentId,

    @Schema(description = "파일의 콘텐츠 타입", example = "image/png")
    @NotEmpty(message = "ContentType is required") String contentType,

    @Schema(description = "파일 데이터를 담은 byte 배열")
    byte[] bytes

) {

  public static BinaryContentDTO fromEntity(BinaryContent binaryContent) {

    return new BinaryContentDTO(
        binaryContent.getId(),
        binaryContent.getContentType(),
        binaryContent.getBytes()
    );
  }
}
