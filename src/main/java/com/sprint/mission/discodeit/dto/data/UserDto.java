package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserDto(
    @NotNull(message = "ID는 필수입니다.")
    UUID id,

    @NotBlank(message = "사용자명은 필수입니다.")
    String username,

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    String email,
    BinaryContentDto profile,

    @NotNull(message = "온라인 상태 값은 필수입니다.")
    Boolean online
) {

}