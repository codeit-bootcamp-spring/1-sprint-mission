package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record BinaryContentDto(
    @NotNull(message = "ID는 필수 입니다.")
    UUID id,
    @NotBlank(message = "파일 이름은 비어있을수 없습니다.")
    @Size(max = 255, message = "파일 이름은 255자를 초과할 수 없습니다.")
    String fileName,
    @NotNull(message = "파일 크기는 필수입니다.")
    @Min(value = 1, message = "파일 크기는 1바이트 이상이어야 합니다")
    Long size,
    @NotBlank(message = "콘텐츠 타입은 비어있을 수 없습니다. ")
    @Size(max = 100, message = "콘텐츠 타입은 100자를 초과할 수 없습니다.")
    String contentType
) {

}