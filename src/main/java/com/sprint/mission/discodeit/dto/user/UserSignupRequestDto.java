package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.security.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSignupRequestDto(@NotBlank String username, @NotBlank String nickname,
                                   @NotBlank @Email String email,
                                   @NotBlank String password, @NotBlank String phone,
                                   @NotNull Phone.RegionCode regionCode, @NotNull Role role,
                                   String introduce) {

}
