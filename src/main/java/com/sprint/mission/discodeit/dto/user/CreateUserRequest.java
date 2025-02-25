package com.sprint.mission.discodeit.dto.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateUserRequest (
        @NotNull
        String name,
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email
        String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[\\W_])(?=.*[a-zA-Z\\d]).{8,15}$", message = "8자리 이상 15자리 이하 대문자 및 특수문자 하나 이상 포함해야 합니다")
        String password,
        UUID profileId) {
}
