package com.sprint.mission.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Schema(example = "홍길동")
        @NotBlank(message = "유저 이름은 필수입니다.")
        @Size(min = 2, max = 10, message = "이름은 2자 이상 6자 이하로 입력해주세요.")
        String username,

        @Schema(example = "비밀번호486")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {

}
