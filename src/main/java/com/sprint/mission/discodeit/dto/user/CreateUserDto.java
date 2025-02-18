package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.Optional;

public record CreateUserDto(
    String password,
    String name,
    String email,
    Optional<BinaryContent> profileImage
) {
}
