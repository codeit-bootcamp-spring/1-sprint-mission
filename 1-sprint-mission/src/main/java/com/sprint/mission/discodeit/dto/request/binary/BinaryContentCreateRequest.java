package com.sprint.mission.discodeit.dto.request.binary;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "BinaryContentCreateRequest", description = "업로드 파일로부터 바이너리 콘텐츠를 생성하기 위한 요청 DTO")
public record BinaryContentCreateRequest(

    @Schema(description = "파일 이름", example = "image.png")
    @NotEmpty(message = "File name is required") String fileName,

    @Schema(description = "파일의 콘텐츠 타입", example = "image/png")
    @NotEmpty(message = "Content type is required") String contentType,

    @Schema(description = "파일 데이터를 담은 byte 배열")
    byte[] bytes
) {

}
