package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record CreateUserRequest(String username, String password, String email) {
}
