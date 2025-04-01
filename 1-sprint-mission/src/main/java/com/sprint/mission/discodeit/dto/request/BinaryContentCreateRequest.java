package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일 이름은 필수입니다.")
    String fileName,
    @NotBlank(message = "파일 Content_Type 은 필수 입니다.")
    String contentType,
    @NotEmpty(message = "파일 데이터는 필수입니다.")
    byte[] bytes
) {

}
