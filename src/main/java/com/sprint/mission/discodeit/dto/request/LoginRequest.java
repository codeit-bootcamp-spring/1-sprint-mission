package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.User;

public record LoginRequest(
    String username,
    String password
) {

}
