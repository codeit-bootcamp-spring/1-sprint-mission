package com.spirnt.mission.discodeit.dto.auth;

public record LoginRequest(
        String name,
        String password
) {
}
