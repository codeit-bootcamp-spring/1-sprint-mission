package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;

public record FindUserDto(UUID id,
                          String name,
                          String email,
                          Optional<BinaryContent> profileImage,
                          UserStatus status) {
}
