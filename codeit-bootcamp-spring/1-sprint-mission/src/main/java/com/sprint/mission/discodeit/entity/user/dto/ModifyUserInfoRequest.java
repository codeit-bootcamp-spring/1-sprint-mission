package com.sprint.mission.discodeit.entity.user.dto;

import java.util.UUID;

public record ModifyUserInfoRequest(UUID id, String changeUsername) {
}
