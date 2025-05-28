package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.role.Role;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
    UUID id,

    @NotBlank
    String username,

    @NotBlank
    String email,

    BinaryContentDto profile,
    Boolean online,
    Role Role
) {

}
