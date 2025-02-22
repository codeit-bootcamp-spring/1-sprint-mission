package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record UpdateUserRequest(UUID id, String username) {

}

