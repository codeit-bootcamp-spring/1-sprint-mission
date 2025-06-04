package com.sprint.mission.discodeit.security.jwt;

import lombok.Getter;

@Getter
public record JwtToken(
    String accessToken,
    String refreshToken
) {
}
