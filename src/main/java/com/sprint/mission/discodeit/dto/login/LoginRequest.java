package com.sprint.mission.discodeit.dto.login;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record LoginRequest(
    @NotBlank(message = "사용자 이름은 필수입니다")
    String userName,
    @NotBlank(message = "비밀번호는 필수입니다")
    String password
) {

}
