package com.sprint.mission.discodeit.dto.user;

import lombok.Builder;

@Builder
public record LoginRequest(String username, String password) {

}
