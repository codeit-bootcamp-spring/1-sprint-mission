package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateDTO
        (
          String newName,
          String newPassword,
          String newEmail,
          String newFilePAth
        )
{
}
