package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.configure.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@NotNull
public record UserDto(
    UUID id,
    String username,

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email,
    BinaryContentDto profile,
    Boolean online,
    Set<Role> roles
) {

}
