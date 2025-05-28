package com.sprint.mission.discodeit.dto.data;

public record TokenPair(
    String accessToken,
    String refreshToken
) {}