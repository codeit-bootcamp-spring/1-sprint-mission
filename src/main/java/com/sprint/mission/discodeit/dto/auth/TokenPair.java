package com.sprint.mission.discodeit.dto.auth;

public record TokenPair(
    String accessToken,
    String refreshToken
) {

}
