package com.sprint.mission.discodeit.entity.user;

import java.util.UUID;

public record UserCommonRequest(
        UUID userId,
        String userName,
        String userEmail,
        String userPassword

)
{
}
