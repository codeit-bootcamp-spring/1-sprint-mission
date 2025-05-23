package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Role;
import java.util.Collection;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online,
    Collection<Role> roles
) {

}
