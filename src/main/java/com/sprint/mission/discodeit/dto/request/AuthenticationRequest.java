package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
    @NotBlank(message = "메일은 필수입니다")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다")
    String password
) {

}
