package com.sprint.mission.discodeit.dto.login;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "로그인 정보")
public record LoginRequest(
    @NotBlank(message = "username 은 필수입니다.")
    String username,

    @NotBlank(message = "password 는 필수입니다.")
    String password
) {

}
