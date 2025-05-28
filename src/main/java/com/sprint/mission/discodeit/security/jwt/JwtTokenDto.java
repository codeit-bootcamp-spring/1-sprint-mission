package com.sprint.mission.discodeit.security.jwt;

public record JwtTokenDto(
    String accessToken,
    String refreshToken
) {

}
