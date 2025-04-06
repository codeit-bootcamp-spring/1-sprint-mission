package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    String username;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password;
}
