package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일 이름은 필수입니다")
    @Size(max = 255, message = "파일 이름은 255자 이하여야 합니다")
    String fileName,
    
    @NotBlank(message = "콘텐츠 타입은 필수입니다")
    String contentType,
    
    @NotNull(message = "파일 데이터는 필수입니다")
    byte[] bytes
) {

}
