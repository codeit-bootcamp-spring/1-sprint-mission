package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "첨부 파일 생성 정보")
public record BinaryContentCreateRequest(
    @Schema(description = "파일 이름", example = "example.jpg")
    String fileName,

    @Schema(description = "콘텐츠 타입", example = "image/jpeg")
    String contentType,

    @Schema(description = "파일 바이트 데이터", example = "U3dhZ2dlciByb2Nrcw==") // Base64 예시
    byte[] bytes
) {

}