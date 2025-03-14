package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.user.UserResponse;
import java.time.Instant;
import java.util.UUID;

public record MessageResponse(UUID id, String text, UserResponse author, UUID channelId,
                              Instant createAt) {

}
