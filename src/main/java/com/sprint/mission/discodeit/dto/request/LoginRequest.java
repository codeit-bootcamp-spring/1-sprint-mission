package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "사용자 이름은 필수입니다.")
    String username,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 3, max = 30, message = "비밀번호는 3자 이상 30자 이하여야 합니다.")
    String password
) {

}
