package com.sprint.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "USER 수정 정보 DTO")
public record UserDtoForUpdate(

    @Schema(description = "새로운 이름", example = "홍길동")
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하로 입력해주세요.")
    String username,

    @Schema(description = "새로운 비밀번호", example = "1234")
    @NotBlank(message = "비밀번호는 필수입니다.")
    String password,

    @Schema(description = "새로운 이메일", example = "code123@codeit.com")
    @Email(message = "이메일 형식이 아닙니다.")
    String email) {

}

