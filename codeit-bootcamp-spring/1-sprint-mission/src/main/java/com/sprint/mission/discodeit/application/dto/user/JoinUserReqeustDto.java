package com.sprint.mission.discodeit.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record JoinUserReqeustDto(
    @NotBlank String nickname,
    @NotBlank String username,
    @Email String email,
    @NotBlank String password,
    @NotNull LocalDate birthDate
) {

}
