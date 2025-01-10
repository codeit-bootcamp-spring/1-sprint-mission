package com.sprint.mission.discodeit.entity.user.dto;

import com.sprint.mission.discodeit.entity.common.Status;
import java.util.UUID;

public record UserInfoResponse(UUID uuid, String username, Status status) {
}
